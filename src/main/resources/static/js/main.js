document.getElementById('formLogin').addEventListener('submit', async (e) => {
    e.preventDefault();

    const usuario = document.getElementById('usuario').value;
    const password = document.getElementById('password').value;

    try {
        const res = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nombreUsuario: usuario, password: password })
        });

        if (res.ok) {
            const data = await res.json();
            sessionStorage.setItem('token', data.token);
            sessionStorage.setItem('rol', data.rol);
            window.location.href = '/dashboard.html';
            sessionStorage.setItem('nombre', data.nombre);

        } else {
            document.getElementById('errorUsuario').textContent = 'Usuario o contraseña incorrectos';
        }
    } catch (err) {
        document.getElementById('errorUsuario').textContent = 'Error de conexión con el servidor';
    }

});