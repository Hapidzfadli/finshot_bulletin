// Navbar
$(document).ready(function() {
    highlightActiveMenuItem();
    handleNavbarScroll();
    setupSearchAnimation();
    fixMobileMenuClosing();
});

function highlightActiveMenuItem() {
    const currentPath  = window.location.pathname;

    $('.navbar .nav-link').each(function() {
        const linkPath = $(this).attr('href');
        if (currentPath === linkPath) {
            $(this).addClass('active fw-bold');
        }
    });
}

function handleNavbarScroll() {
    $(window).scroll(function (){
        if ($(window).scrollTop() > 50) {
            $('.navbar').addClass('scrolled');
        } else {
            $('.navbar').removeClass('scrolled');
        }
    })
    $(window).trigger('scroll');
}

function setupSearchAnimation() {
    $('.search-input')
        .focus(function() {
            $(this).parent().addClass('search-focused');
        })
        .blur(function() {
            $(this).parent().removeClass('search-focused');
        });

    // Handle search form submission
    $('form[role="search"]').on('submit', function(e) {
        e.preventDefault();
        const searchTerm = $('.search-input').val().trim();

        if (searchTerm.length > 0) {
            // Forward to search results page
            window.location.href = '/posts?search=' + encodeURIComponent(searchTerm);
        }
    });
}

function fixMobileMenuClosing() {
    // Close mobile menu when clicking a link
    $('.navbar-nav .nav-link').click(function() {
        const navbarCollapse = $('.navbar-collapse');
        if (navbarCollapse.hasClass('show')) {
            navbarCollapse.collapse('hide');
        }
    });
}
