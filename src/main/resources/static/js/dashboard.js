// Verificar que haya sesión activa; si no, devolver al login
const token = sessionStorage.getItem('token');
const rol = sessionStorage.getItem('rol');

if (!token) {
    window.location.href = '/login.html';
} else {
    document.getElementById('rolUsuario').textContent = rol;
    document.getElementById('nombreUsuario').textContent = sessionStorage.getItem('nombre');
    // El nombre no lo guardamos en sessionStorage todavía, lo agregamos en el paso 4
}

document.getElementById('btnLogout').addEventListener('click', () => {
    sessionStorage.clear();
    window.location.href = '/login.html';
});