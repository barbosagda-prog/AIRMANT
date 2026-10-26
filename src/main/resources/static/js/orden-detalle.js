const token = sessionStorage.getItem('token');
if (!token) window.location.href = '/login.html';

const params = new URLSearchParams(window.location.search);
const idOrden = params.get('id');
if (!idOrden) window.location.href = '/ordenes.html';

const API = `/api/ordenes/${idOrden}`;

async function cargarOrden() {
    const res = await apiFetch(API);
    if (!res.ok) { alert('Orden no encontrada'); window.location.href = '/ordenes.html'; return; }
    const o = await res.json();

    document.getElementById('tituloOrden').textContent = `Orden ${o.numeroOrden ?? ''}`;
    document.getElementById('estadoOrden').textContent = `Estado: ${o.estado}`;
    document.getElementById('resumenOrden').innerHTML = `
        <strong>Aeronave:</strong> ${o.aeronave?.matricula} - ${o.aeronave?.modeloFabricacion} &nbsp;|&nbsp;
        <strong>Técnico:</strong> ${o.tecnicoResponsable?.nombres ?? '-'} &nbsp;|&nbsp;
        <strong>Tipo:</strong> ${o.tipoMantenimiento} &nbsp;|&nbsp;
        <strong>Prioridad:</strong> ${o.prioridad}<br>
        <strong>Motivo:</strong> ${o.motivo}
    `;

    if (o.estado === 'Finalizada') {
        document.getElementById('bloqueCierre').style.display = 'none';
    }
}

async function cargarTareas() {
    const res = await apiFetch(`${API}/tareas`);
    const tareas = await res.json();
    const lista = document.getElementById('listaTareas');
    lista.innerHTML = '';
    tareas.forEach(t => {
        const li = document.createElement('li');
        const badge = t.estado === 'Completada' ? 'badge-completada' : 'badge-pendiente';
        li.innerHTML = `
            <span>${t.descripcion}</span>
            <span>
                <span class="${badge}">${t.estado}</span>
                ${t.estado !== 'Completada' ? `<button data-id="${t.idTarea}" class="btn-completar-tarea btn-editar" style="margin-left:8px;">Completar</button>` : ''}
            </span>
        `;
        lista.appendChild(li);
    });
}

document.getElementById('btnAgregarTarea').addEventListener('click', async () => {
    const descripcion = document.getElementById('nuevaTarea').value;
    if (!descripcion) return;
    await apiFetch(`${API}/tareas`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ descripcion })
    });
    document.getElementById('nuevaTarea').value = '';
    cargarTareas();
});

document.getElementById('listaTareas').addEventListener('click', async (e) => {
    if (e.target.classList.contains('btn-completar-tarea')) {
        const idTarea = e.target.dataset.id;
        await apiFetch(`${API}/tareas/${idTarea}/completar`, { method: 'PUT' });
        cargarTareas();
    }
});

async function cargarDocumentos() {
    const res = await apiFetch(`${API}/documentos`);
    const documentos = await res.json();
    const lista = document.getElementById('listaDocumentos');
    lista.innerHTML = '';
    documentos.forEach(d => {
        const li = document.createElement('li');
        li.innerHTML = `<span><strong>${d.tipoDocumento}</strong> - ${d.codigoReferencia} (${d.numeroRevision ?? 's/r'}) - ${d.capituloSeccion ?? ''}</span>`;
        lista.appendChild(li);
    });
}

document.getElementById('btnAgregarDocumento').addEventListener('click', async () => {
    const datos = {
        tipoDocumento: document.getElementById('tipoDocumento').value,
        codigoReferencia: document.getElementById('codigoReferencia').value,
        numeroRevision: document.getElementById('numeroRevision').value,
        capituloSeccion: document.getElementById('capituloSeccion').value
    };
    if (!datos.codigoReferencia) return;
    await apiFetch(`${API}/documentos`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datos)
    });
    document.getElementById('codigoReferencia').value = '';
    document.getElementById('numeroRevision').value = '';
    document.getElementById('capituloSeccion').value = '';
    cargarDocumentos();
});

async function cargarSelectRepuestos() {
    const res = await apiFetch('/api/repuestos');
    const repuestos = await res.json();
    const select = document.getElementById('selectRepuesto');
    select.innerHTML = '';
    repuestos.forEach(r => {
        select.innerHTML += `<option value="${r.idRepuesto}">${r.codigo} - ${r.descripcion} (stock: ${r.cantidadStock})</option>`;
    });
}

async function cargarRepuestosUsados() {
    const res = await apiFetch(`${API}/repuestos`);
    const usados = await res.json();
    const lista = document.getElementById('listaRepuestos');
    lista.innerHTML = '';
    usados.forEach(u => {
        const li = document.createElement('li');
        li.innerHTML = `<span>${u.repuesto?.codigo} - ${u.repuesto?.descripcion} | Cantidad: ${u.cantidadUtilizada}${u.numeroSerie ? ' | S/N: ' + u.numeroSerie : ''}</span>`;
        lista.appendChild(li);
    });
}

document.getElementById('btnAgregarRepuesto').addEventListener('click', async () => {
    const idRepuesto = document.getElementById('selectRepuesto').value;
    const cantidadUtilizada = document.getElementById('cantidadRepuesto').value;
    const numeroSerie = document.getElementById('numeroSerieRepuesto').value;
    if (!idRepuesto || !cantidadUtilizada) return;

    const res = await apiFetch(`${API}/cerrar?idUsuario=${sessionStorage.getItem('idUsuario')}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datos)
    });

    if (res.ok) {
        document.getElementById('cantidadRepuesto').value = '';
        document.getElementById('numeroSerieRepuesto').value = '';
        cargarRepuestosUsados();
        cargarSelectRepuestos(); // refresca el stock mostrado
    } else {
        const error = await res.text();
        alert(error);
    }
});

async function cargarSelectHerramientas() {
    const res = await apiFetch('/api/herramientas');
    const herramientas = await res.json();
    const select = document.getElementById('selectHerramienta');
    select.innerHTML = '';
    herramientas.forEach(h => {
        select.innerHTML += `<option value="${h.idHerramienta}">${h.codigo} - ${h.nombre} (${h.estadoCalibracion})</option>`;
    });
}

async function cargarHerramientasUsadas() {
    const res = await apiFetch(`${API}/herramientas`);
    const usadas = await res.json();
    const lista = document.getElementById('listaHerramientas');
    lista.innerHTML = '';
    usadas.forEach(u => {
        const li = document.createElement('li');
        li.innerHTML = `<span>${u.herramienta?.codigo} - ${u.herramienta?.nombre} | Fecha de uso: ${u.fechaUso}</span>`;
        lista.appendChild(li);
    });
}

document.getElementById('btnAgregarHerramienta').addEventListener('click', async () => {
    const idHerramienta = document.getElementById('selectHerramienta').value;
    if (!idHerramienta) return;

    const res = await apiFetch(`${API}/herramientas`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ idHerramienta })
    });

    if (res.ok) {
        cargarHerramientasUsadas();
    } else {
        const error = await res.text();
        alert(error);
    }
});

async function cargarSelectInspector() {
    const res = await apiFetch('/api/usuarios');
    const usuarios = await res.json();
    const select = document.getElementById('selectInspector');
    select.innerHTML = '<option value="">Sin inspector</option>';
    usuarios.forEach(u => {
        select.innerHTML += `<option value="${u.id_usuarios}">${u.nombres} ${u.apellidos}</option>`;
    });
}

document.getElementById('btnCerrarOrden').addEventListener('click', async () => {
    if (!confirm('¿Confirmas finalizar esta orden? No podrás editarla después.')) return;

    const datos = {
        trabajoRealizado: document.getElementById('trabajoRealizado').value,
        resultado: document.getElementById('resultado').value,
        observaciones: document.getElementById('observacionesFinales').value,
        idInspector: document.getElementById('selectInspector').value || null
    };

    const res = await apiFetch(`${API}/cerrar?idUsuario=${sessionStorage.getItem('idUsuario')}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datos)
    });

    if (res.ok) {
        alert('Orden finalizada correctamente');
        cargarOrden();
    } else {
        const error = await res.text();
        alert('Error al finalizar la orden: ' + (error || 'código ' + res.status));
    }
});

cargarOrden();
cargarTareas();
cargarDocumentos();
cargarSelectRepuestos();
cargarRepuestosUsados();
cargarSelectHerramientas();
cargarHerramientasUsadas();
cargarSelectInspector();