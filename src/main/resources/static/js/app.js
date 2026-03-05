
document.addEventListener("DOMContentLoaded", () => {

    const params = new URLSearchParams(window.location.search);
    const msg = document.getElementById("msg");

    if (params.get("error") === "true") {
        msg.innerHTML =
            `<div class="alert alert-danger">
                Usuario o contraseña incorrectos
            </div>`;
    }

    if (params.get("logout") === "true") {
        msg.innerHTML =
            `<div class="alert alert-success">
                Sesión cerrada correctamente
            </div>`;
    }

    /* limpiar parámetros de la URL */

    if (params.has("error") || params.has("logout")) {
        window.history.replaceState({}, document.title, window.location.pathname);
    }

    /* ==========================================
       FUNCIONALIDAD: MOSTRAR/OCULTAR CONTRASEÑA
    ========================================== */
    const togglePassword = document.getElementById("togglePassword");
    const passwordField = document.getElementById("passwordField");
    const eyeIcon = document.getElementById("eyeIcon");

    if (togglePassword && passwordField) {
        togglePassword.addEventListener("click", function () {

            const type = passwordField.getAttribute("type") === "password" ? "text" : "password";
            passwordField.setAttribute("type", type);

            eyeIcon.classList.toggle("bi-eye");
            eyeIcon.classList.toggle("bi-eye-slash");

            if (type === "text") {
                eyeIcon.style.color = "#3f6ad8";
            } else {
                eyeIcon.style.color = "#6c757d";
            }
        });
    }
});