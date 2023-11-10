<script setup>
    import { RouterView } from 'vue-router'
</script>

<template>
    <v-main>
        <v-container>
            <!--
               User Component의 내용이 표시됩니다. 
            -->
            <RouterView />
        </v-container>
    </v-main>
</template>

<style scoped>
</style>