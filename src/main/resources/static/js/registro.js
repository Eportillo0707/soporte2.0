document.addEventListener("DOMContentLoaded", () => {

    const form = document.getElementById('registroForm');
    const mensajeDiv = document.getElementById('mensaje');

    form.addEventListener('submit', function(e) {

        e.preventDefault();

        const data = {

            nombre: document.getElementById('nombre').value,
            correo: document.getElementById('correo').value,
            telefono: document.getElementById('telefono').value,
            username: document.getElementById('username').value,
            password: document.getElementById('password').value

        };

        fetch('/auth/registro', {

            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },

            body: JSON.stringify(data)

        })

            .then(async (response) => {

                const text = await response.text();

                if (!response.ok) {
                    throw new Error(text || "Error al registrar usuario");
                }

                return text;

            })

            .then(msg => {

                mensajeDiv.innerHTML =
                    `<div class="alert alert-success">
                ${msg}<br>
                Redirigiendo al login...
            </div>`;

                form.reset();

                setTimeout(() => {
                    window.location.href = "login.html";
                }, 2000);

            })

            .catch(error => {

                mensajeDiv.innerHTML =
                    `<div class="alert alert-danger">
                ${error.message}
            </div>`;

            });

    });

});