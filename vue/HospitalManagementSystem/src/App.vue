<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script setup>
// No specific script needed for App.vue with just router-view
</script>

<style> /* Global styles */
html, body, #app {
  height: 100vh;
  width: 100vw;
  margin: 0;
  padding: 0;
  /* box-sizing: border-box;
  overflow-x: hidden; */
}

@media (min-width: 1920px) {
  #app {
    max-width: 1920px;
    margin: 0 auto;
  }
}
</style>

<style scoped>
#app {
  background-image: url('/images/background.jpg'); /* Corrected path for public assets */
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  width: 100%; /* Ensure #app takes full width */
  height: 100%; /* Ensure #app takes full height */
  display: flex; /* Add flex display */
  align-items: center; /* Vertically center content */
  justify-content: flex-start; /* Horizontally align content to left */
  padding-left: 75px; /* Add left padding to move content slightly left */
  /* min-height: 100vh; is also fine, height: 100% relies on parent having height */
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

/* Example of a global link style if not using a UI library's reset/base */
a {
  text-decoration: none;
  color: inherit;
}
</style>
