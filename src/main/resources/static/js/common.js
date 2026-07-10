document.addEventListener('DOMContentLoaded', function() {
    var userInfo = document.querySelector('.user-info');
    var userDropdown = document.querySelector('.user-dropdown');
    
    if (userInfo) {
        userInfo.addEventListener('click', function(e) {
            e.stopPropagation();
            userInfo.classList.toggle('active');
        });
        
        document.addEventListener('click', function(e) {
            if (!userInfo.contains(e.target)) {
                userInfo.classList.remove('active');
            }
        });
        
        if (userDropdown) {
            userDropdown.addEventListener('click', function(e) {
                e.stopPropagation();
            });
        }
    }
});