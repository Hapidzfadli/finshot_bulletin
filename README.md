# Panduan Deployment Aplikasi Bulletin Board

Dokumen ini berisi panduan lengkap untuk melakukan deployment aplikasi Bulletin Board ke domain bulletin.hapidzfadli.com menggunakan Docker, Nginx, dan SSL.

## Prasyarat

Sebelum memulai proses deployment, pastikan Anda memiliki hal-hal berikut:

1. VPS/Server dengan OS Ubuntu 20.04 atau lebih baru
2. Akses SSH ke server tersebut
3. Domain bulletin.hapidzfadli.com yang sudah diarahkan ke IP server Anda
4. Docker dan Docker Compose terinstal pada server

## Langkah 1: Persiapan Server

### Instalasi Dependensi

```bash
# Update paket
sudo apt update && sudo apt upgrade -y

# Instalasi dependensi
sudo apt install -y nginx certbot python3-certbot-nginx git

# Instalasi Docker jika belum ada
if ! command -v docker &> /dev/null; then
    curl -fsSL https://get.docker.com -o get-docker.sh
    sudo sh get-docker.sh
    sudo usermod -aG docker $USER
    rm get-docker.sh
fi

# Instalasi Docker Compose jika belum ada
if ! command -v docker-compose &> /dev/null; then
    sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    sudo chmod +x /usr/local/bin/docker-compose
fi
```

### Konfigurasi Firewall

```bash
# Konfigurasi UFW (Uncomplicated Firewall)
sudo ufw allow 'Nginx Full'
sudo ufw allow ssh
sudo ufw enable
```

## Langkah 2: Menyiapkan Proyek

### Clone Repository

```bash
# Buat direktori untuk proyek
mkdir -p /var/www
cd /var/www

# Clone repository proyek
git clone https://github.com/Hapidzfadli/web-bulletin-board.git bulletin-board
cd bulletin-board
```

### Menyiapkan File Konfigurasi

Buat file `deploy.sh` untuk mempermudah proses deployment:

```bash
cat > deploy.sh << 'EOF'
#!/bin/bash

# Update sistem
sudo apt update && sudo apt upgrade -y

# Instalasi dependensi sistem
curl -sL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt install -y nodejs npm apache2 mysql-server

# Instalasi PM2 secara global
sudo npm install -g pm2

# Konfigurasi MySQL
sudo mysql -e "CREATE DATABASE IF NOT EXISTS digidesa;"
sudo mysql -e "CREATE USER IF NOT EXISTS 'prisma'@'localhost' IDENTIFIED BY 'digides21';"
sudo mysql -e "GRANT ALL PRIVILEGES ON digidesa.* TO 'prisma'@'localhost';"
sudo mysql -e "FLUSH PRIVILEGES;"

# Konfigurasi environment variables untuk backend
cat << EOT > backend-bulletin-board/.env
DATABASE_URL="mysql://prisma:digides21@localhost:3306/digidesa"
JWT_SECRET=K&18E3w{vhv6{hE_3()&st?4h!4e2r
EOT

# Konfigurasi environment variables untuk frontend
cat << EOT > frontend-bulletin-board/.env
NEXT_PUBLIC_API_URL = ''
EOT

# Instalasi dependensi dan build untuk backend
cd backend-bulletin-board
npm install -g @nestjs/cli
npm install
npx prisma migrate deploy
npm run build

# Instalasi dependensi dan build untuk frontend
cd ../frontend-bulletin-board
npm install
npm run build

# Konfigurasi PM2
cd ../backend-bulletin-board
pm2 start npm --name "backend-bulletin-board" -- start
cd ../frontend-bulletin-board
pm2 start npm --name "frontend-bulletin-board" -- start

pm2 save
pm2 startup

echo "Deployment selesai!"
EOF

chmod +x deploy.sh
```

## Langkah 3: Konfigurasi Nginx

Buat konfigurasi Nginx untuk bulletin.hapidzfadli.com:

```bash
sudo nano /etc/nginx/sites-available/bulletin.hapidzfadli.com
```

Isi dengan konfigurasi berikut:

```nginx
server {
    listen 80;
    server_name bulletin.hapidzfadli.com;
    
    location / {
        proxy_pass http://localhost:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }
    
    location /api {
        proxy_pass http://localhost:3001/api;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }
}
```

Aktifkan konfigurasi dan restart Nginx:

```bash
sudo ln -s /etc/nginx/sites-available/bulletin.hapidzfadli.com /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

## Langkah 4: Menginstal Sertifikat SSL

Gunakan Certbot untuk menginstal SSL otomatis:

```bash
sudo certbot --nginx -d bulletin.hapidzfadli.com
```

Ikuti petunjuk di layar untuk menyelesaikan proses instalasi sertifikat. Certbot akan mengupdate konfigurasi Nginx Anda secara otomatis untuk menggunakan HTTPS.

## Langkah 5: Menjalankan Aplikasi

Jalankan script deployment:

```bash
./deploy.sh
```

Script ini akan:
1. Menginstal semua dependensi yang diperlukan
2. Mengkonfigurasi database MySQL
3. Membuat file environment variables
4. Membangun aplikasi frontend dan backend
5. Mengkonfigurasi PM2 untuk mengelola proses aplikasi

## Langkah 6: Verifikasi Deployment

Setelah proses deployment selesai, aplikasi Bulletin Board Anda seharusnya dapat diakses melalui https://bulletin.hapidzfadli.com.

Untuk memverifikasi bahwa aplikasi berjalan dengan benar:

```bash
# Periksa status PM2
pm2 status

# Periksa log aplikasi
pm2 logs

# Uji akses ke aplikasi
curl -I https://bulletin.hapidzfadli.com
```

## Pemeliharaan

### Memperbarui Aplikasi

Untuk memperbarui aplikasi ke versi terbaru:

```bash
cd /var/www/bulletin-board

# Pull perubahan terbaru
git pull

# Jalankan kembali script deployment
./deploy.sh
```

### Memperbarui Sertifikat SSL

Sertifikat Let's Encrypt akan diperbarui secara otomatis oleh Certbot. Anda dapat memverifikasi ini dengan:

```bash
sudo systemctl status certbot.timer
```

### Backup Database

Untuk membuat backup database:

```bash
# Backup database
mysqldump -u prisma -p digidesa > /var/backups/digidesa_$(date +%Y%m%d).sql
```

## Pemecahan Masalah

### Aplikasi Tidak Dapat Diakses

1. Periksa status Nginx:
   ```bash
   sudo systemctl status nginx
   ```

2. Periksa status aplikasi:
   ```bash
   pm2 status
   ```

3. Periksa log Nginx:
   ```bash
   sudo tail -f /var/log/nginx/error.log
   ```

4. Periksa log aplikasi:
   ```bash
   pm2 logs
   ```

### Sertifikat SSL Kedaluwarsa

Jika sertifikat SSL Anda kedaluwarsa, Anda dapat memperbaruinya secara manual:

```bash
sudo certbot renew
```

## Kesimpulan

Sekarang Anda telah berhasil men-deploy aplikasi Bulletin Board ke domain bulletin.hapidzfadli.com dengan HTTPS. Aplikasi ini menggunakan Nginx sebagai reverse proxy, PM2 untuk manajemen proses, dan Let's Encrypt untuk sertifikat SSL.
