<template>
  <div id="app" class="app-container">
    <LoginPage v-if="!usuarioAutenticado" @login="manejarLogin" />
    <LandingPage v-else @logout="manejarLogout" />
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import LoginPage from './components/LoginPage.vue'
import LandingPage from './components/LandingPage.vue'

export default {
  name: 'App',
  components: {
    LoginPage,
    LandingPage
  },
  setup() {
    const usuarioAutenticado = ref(false)

    const manejarLogin = (datos) => {
      console.log('Usuario autenticado:', datos)
      usuarioAutenticado.value = true
      localStorage.setItem('usuario', JSON.stringify(datos))
    }

    const manejarLogout = () => {
      usuarioAutenticado.value = false
      localStorage.removeItem('usuario')
    }

    onMounted(() => {
      const usuarioGuardado = localStorage.getItem('usuario')
      if (usuarioGuardado) {
        usuarioAutenticado.value = true
      }
    })

    return {
      usuarioAutenticado,
      manejarLogin,
      manejarLogout
    }
  }
}
</script>

<style scoped>
.app-container {
  width: 100%;
  height: 100vh;
  overflow: hidden;
}
</style>
