const token = sessionStorage.getItem('token');
if (!token) window.location.href = '/login.html';

const API = '/api/insumos';
const cuerpoTabla = document.getElementById('cuerpoTablaInsumos');
const modal = document.getElementById('modalInsumo');
const form = document.getElementById('formInsumo');
const DIAS_ALERTA = 30;

function diasRestantes(fecha) {
    if (!fecha) return null;
    const hoy = new Date();
    const venc = new Date(fecha);
    return Math.ceil((venc - hoy) / (1000 * 60 * 60 * 24));
}

async function cargarInsumos() {
    const res = await apiFetch(API);
    const lista = await res.json();
    cuerpoTabla.innerHTML = '';
    let alertas = 0;

    lista.forEach(i => {
        const dias = diasRestantes(i.fechaVencimiento);
        const proximoAVencer = dias !== null && dias <= DIAS_ALERTA;
        if (proximoAVencer) alertas++;

        const fila = document.createElement('tr');
        if (proximoAVencer) fila.classList.add('fila-por-vencer');

        fila.innerHTML = `
            <td>${i.codigo}</td>
            <td>${i.descripcion}</td>
            <td>${i.categoria}</td>
            <td>${i.cantidadStock}</td>
            <td>${i.unidadMedida}</td>
            <td>${i.proveedor ?? '-'}</td>
            <td>${i.estado}</td>
            <td>${i.fechaVencimiento ?? '-'} ${proximoAVencer ? `<span class="badge-vencer">¡${dias}d!</span>` : ''}</td>
            <td>
                <button class="btn-editar" data-id="${i.idInsumo}">Editar</button>
                <button class="btn-eliminar" data-id="${i.idInsumo}">Eliminar</button>
            </td>
        `;
        cuerpoTabla.appendChild(fila);
    });

    const contador = document.getElementById('contadorAlertas');
    if (alertas > 0) {
        contador.textContent = `⚠️ ${alertas} próximo(s) a vencer`;
        contador.style.display = 'inline-block';
    } else {
        contador.style.display = 'none';
    }
}

function abrirModal(insumo = null) {
    form.reset();
    document.getElementById('modalTituloInsumo').textContent = insumo ? 'Editar insumo' : 'Nuevo insumo';
    document.getElementById('idInsumo').value = insumo ? insumo.idInsumo : '';
    if (insumo) {
        Object.keys(insumo).forEach(key => {
            const campo = document.getElementById(key);
            if (campo) campo.value = insumo[key] ?? '';
        });
    }
    modal.style.display = 'flex';
}

document.getElementById('btnNuevoInsumo').addEventListener('click', () => abrirModal());
document.getElementById('btnCancelarInsumo').addEventListener('click', () => modal.style.display = 'none');

form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('idInsumo').value;

    const datos = {
        codigo: document.getElementById('codigo').value,
        descripcion: document.getElementById('descripcion').value,
        categoria: document.getElementById('categoria').value,
        cantidadStock: parseInt(document.getElementById('cantidadStock').value),
        unidadMedida: document.getElementById('unidadMedida').value,
        proveedor: document.getElementById('proveedor').value,
        estado: document.getElementById('estado').value,
        fechaVencimiento: document.getElementById('fechaVencimiento').value || null
    };

    const url = id ? `${API}/${id}?idUsuario=${sessionStorage.getItem('idUsuario')}` : `${API}?idUsuario=${sessionStorage.getItem('idUsuario')}`;
    const metodo = id ? 'PUT' : 'POST';

    await apiFetch(url, {
        method: metodo,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datos)
    });

    modal.style.display = 'none';
    cargarInsumos();
});

cuerpoTabla.addEventListener('click', async (e) => {
    const id = e.target.dataset.id;
    if (!id) return;

    if (e.target.classList.contains('btn-editar')) {
        const res = await apiFetch(`${API}/${id}`);
        const insumo = await res.json();
        abrirModal(insumo);
    }

    if (e.target.classList.contains('btn-eliminar')) {
        if (confirm('¿Eliminar este insumo?')) {
            await apiFetch(`${API}/${id}?idUsuario=${sessionStorage.getItem('idUsuario')}`, { method: 'DELETE' });
            cargarInsumos();
        }
    }
});
document.getElementById('btnExportar').addEventListener('click', async () => {
    const res = await apiFetch('/api/insumos/exportar-excel?dias=30');
    if (!res.ok) {
        alert('No se pudo generar el Excel');
        return;
    }
    const blob = await res.blob();
    const url = URL.createObjectURL(blob);
    const enlace = document.createElement('a');
    enlace.href = url;
    enlace.download = 'insumos_proximos_vencer.xlsx';
    enlace.click();
    URL.revokeObjectURL(url);
});

cargarInsumos();