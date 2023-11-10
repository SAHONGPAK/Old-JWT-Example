<script setup>
    import { ref } from 'vue'
    import { useRouter } from 'vue-router'
    import { useUserStore } from '@/stores/user-store';

    const router = useRouter()
    const userStore = useUserStore()

    const passwordVisible = ref(false)

    const userInfo = ref({
        id: '',
        password: ''
    })

    const login = async () => {
        await userStore.login(userInfo.value)
        router.push("/")
    }

</script>

<template>
        <!--
            https://vuetifyjs.com/en/components/text-fields/#login-form
        -->
        <v-card
            class="login-form mx-auto pa-8"
            elevation="8"
            rounded="lg"
        >

            <div class="text-subtitle-1 text-medium-emphasis">계정</div>

            <v-text-field
                density="compact"
                placeholder="이메일"
                prepend-inner-icon="mdi-email-outline"
                variant="outlined"
                v-model="userInfo.id"
            ></v-text-field>

            <div class="text-subtitle-1 text-medium-emphasis d-flex align-center justify-space-between">
                비밀번호

                <a
                class="text-caption text-decoration-none text-blue"
                href="#"
                rel="noopener noreferrer"
                target="_blank"
                >
                비밀번호를 잊으셨나요?</a>
            </div>

            <v-text-field
                :append-inner-icon="passwordVisible ? 'mdi-eye-off' : 'mdi-eye'"
                :type="passwordVisible ? 'text' : 'password'"
                density="compact"
                placeholder="비밀번호"
                prepend-inner-icon="mdi-lock-outline"
                variant="outlined"
                @click:append-inner="passwordVisible = !passwordVisible"
                v-model="userInfo.password"
            ></v-text-field>

            <v-btn
                block
                class="mb-8"
                color="blue"
                size="large"
                variant="tonal"
                @click="login"
            >
                로그인
            </v-btn>

            <v-btn
                block
                class="mb-8"
                color="green"
                size="large"
                variant="tonal"
                @click="$router.push({name: 'signUp'})"
            >
                회원가입
            </v-btn>
        </v-card>
</template>

<style scoped>
.login-form {
    max-width: 30rem;
    max-height: 23rem;
}
</style>