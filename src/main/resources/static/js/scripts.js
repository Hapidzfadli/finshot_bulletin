// Navbar
$(document).ready(function() {
    highlightActiveMenuItem();
    handleNavbarScroll();
    setupSearchAnimation();
    fixMobileMenuClosing();

    initializePostCards();
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

// Post Card

function initializePostCards() {
    validatePostTitles();
    validateAuthorNames();
    setupPostCardHoverEffects();
    initializePostCardTooltips();
}

function validatePostTitles(){
    $('.post-card .card-title a').each(function (){
       // get title
       const  fullTitle = $(this).attr('title') || $(this).text();
       const titleEl = $(this).find('span');

       let effectiveLength = calculateKoreanAdjustedLength(fullTitle);

       if(effectiveLength > 50) {
           let visibleTitle = truncateByKoreanRules(fullTitle, 50);
           if(visibleTitle.length < fullTitle.length){
               visibleTitle += '...';
           }
           titleEl.text(visibleTitle);
       }

    });
}

function validateAuthorNames() {
    $('.post-card .author-name').each(function() {
        const fullName = $(this).attr('title') || $(this).text();
        const nameEl = $(this).find('span');

        if (fullName.length > 10) {
            const truncatedName = fullName.substring(0, 10) + '...';
            nameEl.text(truncatedName);
        }
    });
}

function calculateKoreanAdjustedLength(text) {
    let koreanCharCount = 0;
    let otherCharCount = 0;

    for (let i = 0; i < text.length; i++) {
        if (isKoreanChar(text.charAt(i))) {
            koreanCharCount++;
        } else {
            otherCharCount++;
        }
    }

    return koreanCharCount + (otherCharCount / 2);
}

function truncateByKoreanRules(text, maxLength) {
    let visibleText = '';
    let currentLength = 0;

    for (let i = 0; i < text.length && currentLength < maxLength; i++) {
        const char = text.charAt(i);
        if (isKoreanChar(char)) {
            currentLength += 1;
        } else {
            currentLength += 0.5;
        }
        visibleText += char;
    }

    return visibleText;
}

function setupPostCardHoverEffects() {
    $('.post-card').hover(
        function() {
            $(this).addClass('card-hover');
        },
        function() {
            $(this).removeClass('card-hover');
        }
    );
}

function initializePostCardTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[title]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        if (tooltipTriggerEl.getAttribute('title') && tooltipTriggerEl.getAttribute('title').trim() !== '') {
            return new bootstrap.Tooltip(tooltipTriggerEl, {
                placement: 'top',
                trigger: 'hover',
                delay: {show: 300, hide: 100}
            });
        }
        return null;
    });
}

function isKoreanChar(char) {
    const code = char.charCodeAt(0);
    return (code >= 0xAC00 && code <= 0xD7A3) ||  // Hangul syllables
        (code >= 0x1100 && code <= 0x11FF) ||  // Hangul Jamo
        (code >= 0x3130 && code <= 0x318F);    // Hangul Compatibility Jamo
}