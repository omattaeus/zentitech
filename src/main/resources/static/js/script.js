$(document).ready(function() {
    // Sidebar toggle functionality
    const body = $('body');
    const sidebar = $('.sidebar');
    const sidebarOpen = $('#sidebarOpen');
    const themeToggle = $('#themeToggle');

    // Abrir e fechar a sidebar
    sidebarOpen.on('click', function() {
        sidebar.toggleClass('close');
    });

    // Alternar o tema claro e escuro
    themeToggle.on('click', function() {
        body.toggleClass('dark');
        $(this).toggleClass('bx-sun bx-moon');
    });

    // Função para lidar com submenus
    $('.submenu_item').on('click', function() {
        $(this).toggleClass('show_submenu');
    });
});
