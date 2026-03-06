document.addEventListener("DOMContentLoaded", () => {

    /* ==========================================
       LÓGICA DEL LOGIN (URLS & PASSWORD)
    ========================================== */
    const params = new URLSearchParams(window.location.search);
    const msg = document.getElementById("msg");

    if (msg) {
        if (params.get("error") === "true") {
            msg.innerHTML = `<div class="alert alert-danger">Usuario o contraseña incorrectos</div>`;
        }
        if (params.get("logout") === "true") {
            msg.innerHTML = `<div class="alert alert-success">Sesión cerrada correctamente</div>`;
        }
        if (params.has("error") || params.has("logout")) {
            window.history.replaceState({}, document.title, window.location.pathname);
        }
    }

    const togglePassword = document.getElementById("togglePassword");
    const passwordField = document.getElementById("passwordField");
    const eyeIcon = document.getElementById("eyeIcon");

    if (togglePassword && passwordField) {
        togglePassword.addEventListener("click", function () {
            const type = passwordField.getAttribute("type") === "password" ? "text" : "password";
            passwordField.setAttribute("type", type);
            eyeIcon.classList.toggle("bi-eye");
            eyeIcon.classList.toggle("bi-eye-slash");
            eyeIcon.style.color = type === "text" ? "#3f6ad8" : "#6c757d";
        });
    }

    /* ==========================================
       INICIALIZACIÓN DEL DASHBOARD (AUTH)
    ========================================== */
    const userNameDisplay = document.getElementById("userNameDisplay");

    if (userNameDisplay) {
        fetch("/auth/me")
            .then(r => {
                if (!r.ok) throw new Error("No autenticado");
                return r.json();
            })
            .then(u => {
                const esAdmin = u.rol === "ADMIN";
                userNameDisplay.innerText = `Hola, ${u.username || 'Usuario'}`;

                if (esAdmin) {
                    document.querySelectorAll('.admin-menu').forEach(el => el.style.display = 'block');
                    document.getElementById('gestionSection').classList.add('active');
                    cargarTodasLasSolicitudes();
                } else {
                    document.querySelectorAll('.user-menu').forEach(el => el.style.display = 'block');
                    document.getElementById('crearSolicitudSection').classList.add('active');
                }
            })
            .catch(() => {
                window.location.href = "/login.html";
            });
    }
});

/* ==========================================
   NAVEGACIÓN DE SECCIONES
========================================== */
function showSection(sectionId, clickedElement) {
    document.querySelectorAll('.content-section').forEach(sec => sec.classList.remove('active'));
    document.querySelectorAll('.sidebar .nav-link').forEach(link => link.classList.remove('active'));

    document.getElementById(sectionId).classList.add('active');
    clickedElement.classList.add('active');
}

/* ==========================================
   UTILIDADES
========================================== */
function colorEstado(estado) {
    if (estado === "PENDIENTE") return "bg-warning text-dark rounded-pill status-badge";
    if (estado === "EN_PROCESO") return "bg-info text-white rounded-pill status-badge";
    if (estado === "FINALIZADO") return "bg-success text-white rounded-pill status-badge";
    return "bg-secondary text-white rounded-pill status-badge";
}

/* ==========================================
   LÓGICA DEL USUARIO
========================================== */
const formCrear = document.getElementById('solicitudForm');
const mensajeCreacionDiv = document.getElementById('mensajeCreacion');
const tablaMisSolicitudes = document.getElementById("tablaMisSolicitudes");

if (formCrear) {
    formCrear.addEventListener('submit', function(e) {
        e.preventDefault();
        const descripcion = document.getElementById('descripcion').value;

        fetch(`/solicitudes/mia?descripcion=${encodeURIComponent(descripcion)}`, { method: 'POST' })
            .then(r => r.json())
            .then(data => {
                mensajeCreacionDiv.innerHTML = `
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>¡Éxito!</strong> Solicitud creada. Estado inicial: <span class="badge bg-secondary">${data.estado}</span>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>`;
                formCrear.reset();
            })
            .catch(() => {
                mensajeCreacionDiv.innerHTML = `
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                   Solicitud simulada creada exitosamente.
                   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>`;
                formCrear.reset();
            });
    });
}

function cargarMisSolicitudes() {
    if(!tablaMisSolicitudes) return;
    tablaMisSolicitudes.innerHTML = '<tr><td colspan="3" class="text-center py-5"><div class="spinner-border text-primary" role="status"></div></td></tr>';

    fetch("/solicitudes/mias")
        .then(r => r.json())
        .then(data => {
            tablaMisSolicitudes.innerHTML = "";
            if (data.length === 0) {
                tablaMisSolicitudes.innerHTML = '<tr><td colspan="3" class="text-center text-muted py-5">No tienes solicitudes registradas.</td></tr>';
                return;
            }
            data.forEach(s => {
                tablaMisSolicitudes.innerHTML += `
                <tr>
                    <td class="ps-4 fw-bold" style="color: #0d6efd;">#${s.id}</td>
                    <td class="fw-medium">${s.descripcion}</td>
                    <td><span class="badge ${colorEstado(s.estado)}">${s.estado}</span></td>
                </tr>`;
            });
        })
        .catch(err => {
            tablaMisSolicitudes.innerHTML = '<tr><td colspan="3" class="text-center text-danger py-4">Error al cargar los datos.</td></tr>';
        });
}

/* ==========================================
   LÓGICA DEL ADMINISTRADOR
========================================== */
const tablaGestion = document.getElementById("tablaGestionSolicitudes");

function cargarTodasLasSolicitudes() {
    if(!tablaGestion) return;
    tablaGestion.innerHTML = '<tr><td colspan="4" class="text-center py-5"><div class="spinner-border text-primary" role="status"></div></td></tr>';

    fetch("/solicitudes")
        .then(r => {
            if (!r.ok) throw new Error("Error al obtener datos");
            return r.json();
        })
        .then(data => {
            tablaGestion.innerHTML = "";
            if (data.length === 0) {
                tablaGestion.innerHTML = '<tr><td colspan="4" class="text-center text-muted py-5">No hay solicitudes registradas en el sistema.</td></tr>';
                return;
            }
            data.forEach(s => {
                tablaGestion.innerHTML += `
                <tr>
                    <td class="ps-4 fw-bold" style="color: #0d6efd;">#${s.id}</td>
                    <td class="fw-medium">${s.descripcion}</td>
                    <td><span class="badge ${colorEstado(s.estado)}">${s.estado}</span></td>
                    <td>
                        <div class="d-flex align-items-center gap-2">
                            <select class="form-select form-select-sm select-estado" id="estadoSelect-${s.id}" style="width: auto; min-width: 130px;">
                                <option value="PENDIENTE" ${s.estado === 'PENDIENTE' ? 'selected' : ''}>Pendiente</option>
                                <option value="EN_PROCESO" ${s.estado === 'EN_PROCESO' ? 'selected' : ''}>En Proceso</option>
                                <option value="FINALIZADO" ${s.estado === 'FINALIZADO' ? 'selected' : ''}>Finalizado</option>
                            </select>
                            <button class="btn btn-sm btn-primary rounded-3 px-2 shadow-sm" onclick="actualizarEstado(${s.id})" title="Guardar Cambios">
                                <i class="bi bi-check-lg"></i>
                            </button>
                        </div>
                    </td>
                </tr>`;
            });
        })
        .catch(err => {
            tablaGestion.innerHTML = '<tr><td colspan="4" class="text-center text-danger py-4">Error al cargar los datos.</td></tr>';
        });
}

function actualizarEstado(id) {
    const nuevoEstado = document.getElementById(`estadoSelect-${id}`).value;

    fetch(`/solicitudes/${id}/estado?estado=${nuevoEstado}`, { method: 'PUT' })
        .then(r => {
            if (r.ok) {
                alert(`Ticket #${id} actualizado a ${nuevoEstado}`);
                cargarTodasLasSolicitudes();
            } else {
                alert("Hubo un problema al actualizar el estado.");
            }
        })
        .catch(err => {
            alert("Error de conexión con el servidor.");
        });
}