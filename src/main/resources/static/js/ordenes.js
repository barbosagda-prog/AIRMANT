const token = sessionStorage.getItem('token');
if (!token) window.location.href = '/login.html';

const API_ORDENES = '/api/ordenes';
const cuerpoTabla = document.getElementById('cuerpoTablaOrdenes');
const modal = document.getElementById('modalOrden');
const form = document.getElementById('formOrden');
const selectAeronave = document.getElementById('selectAeronave');
const selectTecnico = document.getElementById('selectTecnico');
const infoAeronave = document.getElementById('infoAeronave');

async function cargarOrdenes() {
    const matricula = document.getElementById('filtroMatricula').value;
    const estado = document.getElementById('filtroEstado').value;

    let url = API_ORDENES;
    if (matricula) url = `${API_ORDENES}/buscar?matricula=${matricula}`;
    else if (estado) url = `${API_ORDENES}/buscar?estado=${estado}`;

    const res = await fetch(url);
    const lista = await res.json();
    cuerpoTabla.innerHTML = '';
    lista.forEach(o => {
        const fila = document.createElement('tr');
        fila.innerHTML = `
            <td>${o.numeroOrden ?? '-'}</td>
            <td>${o.aeronave?.matricula ?? '-'}</td>
            <td>${o.tipoMantenimiento}</td>
            <td>${o.prioridad}</td>
            <td>${o.estado}</td>
            <td>${o.tecnicoResponsable?.nombres ?? '-'}</td>
            <td>${o.fechaCreacion ?? '-'}</td>
            <td><a href="/orden-detalle.html?id=${o.idOrden}" class="btn-editar" style="text-decoration:none; padding:6px 10px; border-radius:6px;">Ver</a></td>
        `;
        cuerpoTabla.appendChild(fila);
    });
}

async function cargarSelects() {
    const resAeronaves = await fetch('/api/aeronaves');
    const aeronaves = await resAeronaves.json();
    selectAeronave.innerHTML = '<option value="">Selecciona una aeronave</option>';
    aeronaves.forEach(a => {
        selectAeronave.innerHTML += `<option value="${a.idAeronave}" data-info='${JSON.stringify(a)}'>${a.matricula} - ${a.modeloFabricacion}</option>`;
    });

    const resUsuarios = await fetch('/api/usuarios');
    const usuarios = await resUsuarios.json();
    const tecnicos = usuarios.filter(u => (u.rol || '').toUpperCase() === 'TECNICO');
    selectTecnico.innerHTML = '<option value="">Selecciona un técnico</option>';
    tecnicos.forEach(t => {
        selectTecnico.innerHTML += `<option value="${t.id_usuarios}">${t.nombres} ${t.apellidos}</option>`;
    });
}

selectAeronave.addEventListener('change', () => {
    const opcion = selectAeronave.selectedOptions[0];
    if (!opcion || !opcion.dataset.info) {
        infoAeronave.style.display = 'none';
        return;
    }
    const a = JSON.parse(opcion.dataset.info);
    infoAeronave.innerHTML = `
        <strong>Modelo:</strong> ${a.modeloFabricacion} &nbsp;|&nbsp;
        <strong>N° Serie:</strong> ${a.numeroSerie} &nbsp;|&nbsp;
        <strong>Horas de vuelo:</strong> ${a.horasVuelo}
    `;
    infoAeronave.style.display = 'block';
});

document.getElementById('btnNuevaOrden').addEventListener('click', async () => {
    await cargarSelects();
    modal.style.display = 'flex';
});
document.getElementById('btnCancelarOrden').addEventListener('click', () => modal.style.display = 'none');
document.getElementById('filtroMatricula').addEventListener('input', cargarOrdenes);
document.getElementById('filtroEstado').addEventListener('change', cargarOrdenes);

form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const datos = {
        idAeronave: parseInt(selectAeronave.value),
        idTecnicoResponsable: parseInt(selectTecnico.value),
        tipoMantenimiento: document.getElementById('tipoMantenimiento').value,
        prioridad: document.getElementById('prioridad').value,
        motivo: document.getElementById('motivo').value,
        descripcionTrabajo: document.getElementById('descripcionTrabajo').value
    };

    const res = await fetch(API_ORDENES, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datos)
    });

    if (res.ok) {
        const creada = await res.json();
        modal.style.display = 'none';
        form.reset();
        window.location.href = `/orden-detalle.html?id=${creada.idOrden}`;
    } else {
        alert('Error al crear la orden. Revisa los datos.');
    }
});

cargarOrdenes();