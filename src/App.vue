<template>
  <div id="app">
    <Home></Home>
    <BackTop></BackTop>
  </div>
</template>

<script>
import Home from '@/components/Home'

export default {
  name: 'app',
  components: {
    'Home': Home
  }
}
</script>

<style>
#app {
  /* font-family: 'Avenir', Helvetica, Arial, sans-serif; */
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background: #eeeeee;
}
</style>
