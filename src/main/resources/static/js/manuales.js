const token = sessionStorage.getItem('token');
if (!token) window.location.href = '/login.html';

const API = '/api/manuales';
const cuerpoTabla = document.getElementById('cuerpoTablaManuales');
const modal = document.getElementById('modalManual');
const modalHistorial = document.getElementById('modalHistorial');
const form = document.getElementById('formManual');
const selectRegistrador = document.getElementById('selectRegistrador');

async function cargarManuales() {
    const res = await fetch(API);
    const lista = await res.json();
    cuerpoTabla.innerHTML = '';
    lista.forEach(m => {
        const fila = document.createElement('tr');
        fila.innerHTML = `
            <td>${m.tipoManual}</td>
            <td>${m.fabricante}</td>
            <td>${m.modelo}</td>
            <td>${m.numeroAta ?? '-'}</td>
            <td>${m.numeroDocumento}</td>
            <td>${m.registradoPor?.nombres ?? '-'}</td>
            <td>${m.fechaRegistro ?? '-'}</td>
            <td><button class="btn-ver-historial btn-editar" data-id="${m.idManual}">Historial</button></td>
        `;
        cuerpoTabla.appendChild(fila);
    });
}

async function cargarSelectRegistrador() {
    const res = await fetch('/api/usuarios');
    const usuarios = await res.json();
    selectRegistrador.innerHTML = '<option value="">Selecciona un usuario</option>';
    usuarios.forEach(u => {
        selectRegistrador.innerHTML += `<option value="${u.id_usuarios}">${u.nombres} ${u.apellidos} (${u.rol})</option>`;
    });
}

document.getElementById('btnNuevoManual').addEventListener('click', async () => {
    form.reset();
    await cargarSelectRegistrador();
    modal.style.display = 'flex';
});
document.getElementById('btnCancelarManual').addEventListener('click', () => modal.style.display = 'none');

form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const datos = {
        tipoManual: document.getElementById('tipoManual').value,
        fabricante: document.getElementById('fabricante').value,
        modelo: document.getElementById('modelo').value,
        numeroAta: document.getElementById('numeroAta').value,
        idioma: document.getElementById('idioma').value,
        numeroDocumento: document.getElementById('numeroDocumento').value,
        fuente: document.getElementById('fuente').value,
        idRegistradoPor: selectRegistrador.value
    };

    const res = await fetch(API, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datos)
    });

    if (res.ok) {
        modal.style.display = 'none';
        cargarManuales();
    } else {
        const error = await res.text();
        alert(error);
    }
});

cuerpoTabla.addEventListener('click', async (e) => {
    if (e.target.classList.contains('btn-ver-historial')) {
        const id = e.target.dataset.id;
        const res = await fetch(`${API}/${id}/historial`);
        const historial = await res.json();
        const lista = document.getElementById('listaHistorial');
        lista.innerHTML = '';
        if (historial.length === 0) {
            lista.innerHTML = '<li>Sin movimientos registrados</li>';
        } else {
            historial.forEach(h => {
                const li = document.createElement('li');
                li.innerHTML = `<span><strong>${h.accion}</strong> por ${h.usuario?.nombres ?? '-'} — ${h.fecha} — ${h.detalle ?? ''}</span>`;
                lista.appendChild(li);
            });
        }
        modalHistorial.style.display = 'flex';
    }
});

document.getElementById('btnCerrarHistorial').addEventListener('click', () => {
    modalHistorial.style.display = 'none';
});

cargarManuales();