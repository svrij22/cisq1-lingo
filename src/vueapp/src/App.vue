<template>
  <div id="app">
    <app-frame/>
  </div>
</template>

<script>

import AppFrame from "./components/AppFrame";
export default {
  name: 'App',
  components: {
    AppFrame,
  },
  watch:{
    $route (to){
      if (to !== '/auth'){
        if (localStorage.getItem("auth") == null){
          this.$router.push("/auth")
        }
      }
    }
  }
}
</script>

<style>
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }

  img{
    width: 400px;
  }
</style>
