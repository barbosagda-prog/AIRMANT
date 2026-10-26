// Utilidad compartida: agrega el token a cada petición y maneja sesión vencida o sin permiso
function apiFetch(url, opciones = {}) {
    const token = sessionStorage.getItem('token');
    const cabeceras = { ...(opciones.headers || {}) };
    if (token) cabeceras['Authorization'] = 'Bearer ' + token;

    return window.fetch(url, { ...opciones, headers: cabeceras }).then(res => {
        if (res.status === 401) {
            sessionStorage.clear();
            window.location.href = '/login.html';
        }
        if (res.status === 403) {
            alert('No tienes permiso para realizar esta acción.');
        }
        return res;
    });
}