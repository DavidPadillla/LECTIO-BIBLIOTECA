// progreso.js - Gestión del progreso de lectura

const ProgresoAPI = {
    baseURL: '/api/progreso',

    /**
     * Obtiene el progreso del usuario actual
     */
    async obtenerProgreso() {
        try {
            const response = await fetch(`${this.baseURL}/mi-progreso`, {
                method: 'GET',
                credentials: 'include' // Incluye cookies de sesión
            });

            if (!response.ok) {
                if (response.status === 401) {
                    console.log('Usuario no autenticado');
                    return null;
                }
                throw new Error('Error al obtener progreso');
            }

            return await response.json();
        } catch (error) {
            console.error('Error:', error);
            return null;
        }
    },

    /**
     * Marca un libro como completado
     */
    async completarLibro(libroId) {
        try {
            const response = await fetch(`${this.baseURL}/completar`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include',
                body: JSON.stringify({ libroId: libroId })
            });

            if (!response.ok) {
                if (response.status === 401) {
                    console.log('Usuario no autenticado');
                    return null;
                }
                throw new Error('Error al completar libro');
            }

            return await response.json();
        } catch (error) {
            console.error('Error:', error);
            return null;
        }
    },

    /**
     * Verifica si un libro está completado
     */
    async verificarLibro(libroId) {
        try {
            const response = await fetch(`${this.baseURL}/verificar/${libroId}`, {
                method: 'GET',
                credentials: 'include'
            });

            if (!response.ok) {
                return { completado: false };
            }

            return await response.json();
        } catch (error) {
            console.error('Error:', error);
            return { completado: false };
        }
    }
};

/**
 * Muestra el progreso del usuario en la página
 */
async function mostrarProgreso() {
    const progreso = await ProgresoAPI.obtenerProgreso();

    if (!progreso) {
        console.log('No hay progreso disponible');
        return;
    }

    // Actualizar UI con el progreso
    const progresoContainer = document.getElementById('progreso-usuario');
    if (progresoContainer) {
        progresoContainer.innerHTML = `
            <div class="progreso-card">
                <h3>📊 Tu Progreso</h3>
                <p><strong>Libros completados:</strong> ${progreso.totalLibrosLeidos}</p>
                <p><strong>Puntos:</strong> ${progreso.puntos} 🏆</p>
            </div>
        `;
    }
}

/**
 * Marca un libro como completado y muestra mensaje
 */
async function marcarComoCompletado(libroId, nombreLibro) {
    const resultado = await ProgresoAPI.completarLibro(libroId);

    if (resultado) {
        // Mostrar notificación de éxito
        mostrarNotificacion(`¡Felicidades! Has completado "${nombreLibro}" 🎉`);

        // Actualizar progreso en pantalla
        mostrarProgreso();
    } else {
        mostrarNotificacion('Debes iniciar sesión para guardar tu progreso', 'warning');
    }
}

/**
 * Verifica y marca libros completados automáticamente
 */
async function verificarLibrosCompletados() {
    const libros = ['principito', '1984', 'harrypotter'];

    for (const libroId of libros) {
        const resultado = await ProgresoAPI.verificarLibro(libroId);

        if (resultado.completado) {
            // Marcar visualmente como completado
            marcarLibroComoCompletadoUI(libroId);
        }
    }
}

/**
 * Marca un libro como completado en la UI
 */
function marcarLibroComoCompletadoUI(libroId) {
    const libroCard = document.querySelector(`[data-libro="${libroId}"]`);
    if (libroCard) {
        libroCard.classList.add('completado');

        // Agregar badge de completado
        if (!libroCard.querySelector('.badge-completado')) {
            const badge = document.createElement('span');
            badge.className = 'badge-completado';
            badge.innerHTML = '✓ Completado';
            libroCard.appendChild(badge);
        }
    }
}

/**
 * Muestra notificaciones al usuario
 */
function mostrarNotificacion(mensaje, tipo = 'success') {
    const notificacion = document.createElement('div');
    notificacion.className = `notificacion notificacion-${tipo}`;
    notificacion.textContent = mensaje;

    document.body.appendChild(notificacion);

    // Animación de entrada
    setTimeout(() => notificacion.classList.add('mostrar'), 100);


    setTimeout(() => {
        notificacion.classList.remove('mostrar');
        setTimeout(() => notificacion.remove(), 300);
    }, 3000);
}


document.addEventListener('DOMContentLoaded', () => {
    mostrarProgreso();
    verificarLibrosCompletados();
});
