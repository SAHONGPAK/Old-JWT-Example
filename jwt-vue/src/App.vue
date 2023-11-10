<script setup>
  import { RouterView } from 'vue-router'
  import HeaderComponent from '@/components/includes/HeaderComponent.vue';
</script>

<template>
  <v-layout>
    <header>
      <HeaderComponent />
    </header>

    <RouterView />
  </v-layout>
</template>

<style scoped>
</style>
