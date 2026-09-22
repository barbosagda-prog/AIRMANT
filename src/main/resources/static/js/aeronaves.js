const token = sessionStorage.getItem('token');
if (!token) window.location.href = '/login.html';

const API = '/api/aeronaves';
const cuerpoTabla = document.getElementById('cuerpoTabla');
const modal = document.getElementById('modalForm');
const form = document.getElementById('formAeronave');

async function cargarAeronaves() {
    const res = await fetch(API);
    const lista = await res.json();
    cuerpoTabla.innerHTML = '';
    lista.forEach(a => {
        const fila = document.createElement('tr');
        fila.innerHTML = `
            <td>${a.matricula}</td>
            <td>${a.fabricante}</td>
            <td>${a.modeloFabricacion}</td>
            <td>${a.tipoAeronave}</td>
            <td>${a.estado}</td>
            <td>${a.horasVuelo}</td>
            <td>
                <button class="btn-editar" data-id="${a.idAeronave}">Editar</button>
                <button class="btn-eliminar" data-id="${a.idAeronave}">Eliminar</button>
            </td>
        `;
        cuerpoTabla.appendChild(fila);
    });
}

function abrirModal(aeronave = null) {
    form.reset();
    document.getElementById('modalTitulo').textContent = aeronave ? 'Editar aeronave' : 'Nueva aeronave';
    document.getElementById('idAeronave').value = aeronave ? aeronave.idAeronave : '';
    if (aeronave) {
        Object.keys(aeronave).forEach(key => {
            const campo = document.getElementById(key);
            if (campo) campo.value = aeronave[key] ?? '';
        });
    }
    modal.style.display = 'flex';
}

document.getElementById('btnNuevo').addEventListener('click', () => abrirModal());
document.getElementById('btnCancelar').addEventListener('click', () => modal.style.display = 'none');

form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('idAeronave').value;

    const datos = {
        matricula: document.getElementById('matricula').value,
        numeroSerie: document.getElementById('numeroSerie').value,
        fabricante: document.getElementById('fabricante').value,
        modeloFabricacion: document.getElementById('modeloFabricacion').value,
        tipoAeronave: document.getElementById('tipoAeronave').value,
        categoria: document.getElementById('categoria').value,
        tipoMotor: document.getElementById('tipoMotor').value,
        numeroMotor: parseInt(document.getElementById('numeroMotor').value),
        pesoMaximoDespegue: parseFloat(document.getElementById('pesoMaximoDespegue').value),
        capacidad: parseInt(document.getElementById('capacidad').value),
        configuracionCabina: document.getElementById('configuracionCabina').value,
        anioFabricacion: parseInt(document.getElementById('anioFabricacion').value),
        estado: document.getElementById('estado').value,
        horasVuelo: parseFloat(document.getElementById('horasVuelo').value),
        proximaInspeccion: document.getElementById('proximaInspeccion').value || null
    };

    const url = id ? `${API}/${id}` : API;
    const metodo = id ? 'PUT' : 'POST';

    await fetch(url, {
        method: metodo,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datos)
    });

    modal.style.display = 'none';
    cargarAeronaves();
});

cuerpoTabla.addEventListener('click', async (e) => {
    const id = e.target.dataset.id;
    if (!id) return;

    if (e.target.classList.contains('btn-editar')) {
        const res = await fetch(`${API}/${id}`);
        const aeronave = await res.json();
        abrirModal(aeronave);
    }

    if (e.target.classList.contains('btn-eliminar')) {
        if (confirm('¿Eliminar esta aeronave?')) {
            await fetch(`${API}/${id}`, { method: 'DELETE' });
            cargarAeronaves();
        }
    }
});

cargarAeronaves();