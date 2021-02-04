<template>
  <div id="app">
    <img alt="Vue logo" src="./assets/logo.png">
    <h2>Lingo</h2>
    <lingo-comp/>
  </div>
</template>

<script>

import LingoComp from "./components/LingoComp";
export default {
  name: 'App',
  components: {
    LingoComp,
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
  margin-top: 60px;
}
</style>
