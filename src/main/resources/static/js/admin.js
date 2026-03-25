// ===== SIDEBAR TOGGLE =====
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const main = document.getElementById('mainContent');
    if (window.innerWidth <= 768) {
        sidebar.classList.toggle('open');
    } else {
        sidebar.classList.toggle('collapsed');
        main.classList.toggle('expanded');
    }
}

// ===== AUTO-DISMISS ALERTS =====
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.alert-dismissible').forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.4s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 400);
        }, 4000);
    });
});

// ===== CONFIRM DELETE =====
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('form[data-confirm]').forEach(form => {
        form.addEventListener('submit', e => {
            if (!confirm(form.dataset.confirm)) e.preventDefault();
        });
    });
});

// ===== FORMAT numbers =====
function formatVND(amount) {
    return new Intl.NumberFormat('vi-VN').format(amount) + '₫';
}

// ===== Close modal on backdrop click =====
document.addEventListener('click', e => {
    const modal = document.getElementById('cancelModal');
    if (modal && e.target === modal) {
        modal.style.display = 'none';
    }
});

// ===== Responsive: close sidebar on outside click (mobile) =====
document.addEventListener('click', e => {
    const sidebar = document.getElementById('sidebar');
    const toggle = document.querySelector('.sidebar-toggle');
    if (window.innerWidth <= 768 && sidebar &&
        sidebar.classList.contains('open') &&
        !sidebar.contains(e.target) &&
        toggle && !toggle.contains(e.target)) {
        sidebar.classList.remove('open');
    }
});
