/**
 * Application Global JavaScript
 * Sidebar toggling, Notification bell polling, Toast alerts
 */

document.addEventListener('DOMContentLoaded', () => {
    // Sidebar toggle for mobile
    const toggleBtn = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('appSidebar');

    if (toggleBtn && sidebar) {
        toggleBtn.addEventListener('click', () => {
            sidebar.classList.toggle('show');
        });

        // Close sidebar when clicking outside on mobile
        document.addEventListener('click', (e) => {
            if (window.innerWidth <= 992) {
                if (!sidebar.contains(e.target) && !toggleBtn.contains(e.target)) {
                    sidebar.classList.remove('show');
                }
            }
        });
    }

    // Auto-dismiss alert messages after 5 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            alert.style.transition = 'opacity 0.5s ease';
            setTimeout(() => alert.remove(), 500);
        }, 5000);
    });

    // Notification Unread Count Auto-refresh (AJAX)
    const notifBadge = document.getElementById('notifCountBadge');
    if (notifBadge) {
        const updateNotifCount = () => {
            const contextPath = document.body.dataset.contextPath || '';
            fetch(`${contextPath}/api/notifications/count`)
                .then(res => res.json())
                .then(data => {
                    if (data && typeof data.unreadCount !== 'undefined') {
                        if (data.unreadCount > 0) {
                            notifBadge.textContent = data.unreadCount;
                            notifBadge.style.display = 'inline-block';
                        } else {
                            notifBadge.style.display = 'none';
                        }
                    }
                })
                .catch(() => {});
        };

        // Poll every 30 seconds
        setInterval(updateNotifCount, 30000);
    }
});
