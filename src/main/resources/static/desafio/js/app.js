const api = "http://localhost:8080/api/desafio";

// Inicializar al cargar la página
window.addEventListener("DOMContentLoaded", async () => {
    console.log("Inicializando desafío...");
    try {
        // Inicializar progreso del usuario autenticado
        const initResponse = await fetch(`${api}/inicializar`, {
            method: "POST",
            credentials: 'include',  // Importante para enviar cookies de sesión
            headers: {"Content-Type": "application/json"}
        });

        const initData = await initResponse.json();
        console.log("Progreso inicializado:", initData);

        // Cargar progreso actual
        await actualizarProgreso();
    } catch (error) {
        console.error("Error al inicializar:", error);
        mostrarError("Error al conectar con el servidor");
    }
});

async function abrirLectura(num, nodo) {
    document.getElementById('modalLectura').style.display = 'flex';
    document.getElementById('tituloLectura').innerText = `Capítulo ${num}`;
    document.getElementById('contenidoLectura').innerText =
        `Contenido del capítulo ${num}. ¡Disfruta tu lectura!`;

    // Marcar como leído
    try {
        const response = await fetch(`${api}/marcar-lectura`, {
            method: "POST",
            credentials: 'include',  // Enviar cookies de sesión
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                capitulo: `Capítulo ${num}`,
                libro: "Libro Actual"
            })
        });

        const data = await response.json();
        console.log("Lectura registrada:", data);

        if (data.success) {
            await actualizarProgreso();
            nodo.classList.add("leido");
            mostrarNotificacion("¡Capítulo completado! 📚");
        }
    } catch (error) {
        console.error("Error al marcar lectura:", error);
        mostrarError("Error al guardar progreso");
    }
}

function cerrarLectura() {
    document.getElementById('modalLectura').style.display = 'none';
}

async function actualizarProgreso() {
    try {
        const response = await fetch(`${api}/progreso`, {
            credentials: 'include'  // Enviar cookies de sesión
        });

        const data = await response.json();
        console.log("Progreso actual:", data);

        // Actualizar elementos del DOM
        const capitulosElem = document.getElementById('capitulosLeidos');
        const diasElem = document.getElementById('daysRead');
        const mensajeElem = document.getElementById('mensajeMotivacional');

        if (capitulosElem) {
            capitulosElem.innerText = data.capitulosLeidos || 0;
        }

        if (diasElem) {
            diasElem.innerText = data.daysRead || 0;
        }

        if (mensajeElem) {
            mensajeElem.innerText = data.mensaje || "¡Comienza a leer!";
        }

    } catch (error) {
        console.error("Error al actualizar progreso:", error);
    }
}

function mostrarNotificacion(mensaje) {
    const notif = document.createElement('div');
    notif.className = 'notificacion-exito';
    notif.textContent = mensaje;
    notif.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: #4CAF50;
        color: white;
        padding: 15px 25px;
        border-radius: 8px;
        box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        z-index: 10000;
        animation: slideIn 0.3s ease-out;
    `;

    document.body.appendChild(notif);

    setTimeout(() => {
        notif.style.animation = 'slideOut 0.3s ease-in';
        setTimeout(() => notif.remove(), 300);
    }, 3000);
}

function mostrarError(mensaje) {
    const notif = document.createElement('div');
    notif.className = 'notificacion-error';
    notif.textContent = mensaje;
    notif.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: #f44336;
        color: white;
        padding: 15px 25px;
        border-radius: 8px;
        box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        z-index: 10000;
    `;

    document.body.appendChild(notif);
    setTimeout(() => notif.remove(), 3000);
}

// Estilos CSS para animaciones
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }

    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);
