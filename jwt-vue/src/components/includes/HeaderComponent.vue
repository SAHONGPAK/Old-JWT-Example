<script setup>
    import { useUserStore } from '@/stores/user-store';
    import { storeToRefs } from 'pinia';
    import router from '../../router';

    const userStore = useUserStore()
    const { menuList } = storeToRefs(userStore)

    const logout = async () => {
        await userStore.logout()
        router.push('/')
    }
</script>

<template>
    <!--
        https://vuetifyjs.com/en/components/app-bars/
        https://vuetifyjs.com/en/components/app-bars/#anatomy
    -->
    <v-app-bar>
        <v-app-bar-nav-icon icon="mdi-menu" />

        <v-app-bar-title 
            text="JWT Example"
            class="v-col-3"
            @click="$router.push('/')"
        />

        <!--
            https://vuetifyjs.com/en/components/lists/
        -->
        <v-list class="v-col-8">
            <!--
                https://vuetifyjs.com/en/components/grids/#v-row
            -->
            <v-row class="justify-end">
                <template v-for="menu in menuList" :key="menu.name">
                    <template v-if="menu.show">
                        <template v-if="menu.routeName === 'logout'">'
                            <v-list-item>
                                <RouterLink
                                    to="/"
                                    @click.prevent="logout"
                                    class="menu"
                                >
                                    {{ menu.name }}
                                </RouterLink>
                            </v-list-item>
                        </template>

                        <template v-else>
                            <v-list-item>
                                <RouterLink
                                    :to="{name: menu.routeName}"
                                    class="menu"
                                >
                                    {{ menu.name }}
                                </RouterLink>
                            </v-list-item>
                        </template>
                    </template>
                </template>
            </v-row>
        </v-list>
    </v-app-bar>
</template>

<style scoped>
</style>