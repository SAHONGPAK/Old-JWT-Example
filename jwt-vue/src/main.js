import { createApp } from 'vue'
import { createPinia } from 'pinia'

// pinia-plugin-persistedstate
import piniaPluginPersistedState from 'pinia-plugin-persistedstate'


import App from './App.vue'
import router from './router'

/************** Vuetify **************/
// https://vuetifyjs.com/en/getting-started/installation/#existing-projects
// npm install vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

// Material-Icon
// https://vuetifyjs.com/en/features/icon-fonts/#installing-icon-fonts
// npm install @mdi/font -D
import '@mdi/font/css/materialdesignicons.css'
import { aliases, mdi } from 'vuetify/iconsets/mdi'

const vuetify = createVuetify({
    components,
    directives,
    // Material-Icon
    icons: {
        defaultSet: 'mdi',
        aliases,
        sets: {
            mdi,
        },
    },
})

/*************************************/

const app = createApp(App)


/*************** AXIOS ***************/
import { instance } from '@/api/axios'
app.provide('axios', instance)

/*************************************/



app.use(createPinia().use(piniaPluginPersistedState))
app.use(router)
app.use(vuetify)

app.mount('#app')
