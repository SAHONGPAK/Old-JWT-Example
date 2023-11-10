<script setup>
    import { ref, inject } from 'vue'
    import { useRouter } from 'vue-router'
    import { useUserStore } from '@/stores/user-store';
    
    const axios = inject('axios')
    const router = useRouter()
    const userStore = useUserStore()

    const currentPasswordVisible = ref(false)
    const newPasswordVisible = ref(false)

    const userInfo = ref({
        id: '',
        name: '',
        currentPassword: '',
        newPassword: ''
    })

    // Setup Script가 실행되면서 바로 사용자 정보를 요청.
    axios.get('/user/myPage')
    .then( (response) => {
        userInfo.value = response.data.userInfo
    })
    .catch( () => {
        router.push('/')
    })
    
    // 회원 수정
    const modify = async () => {
        await userStore.modify(userInfo.value)
        .finally(() => {
            router.push('/')
        })
    }

    // 회원 탈퇴
    const withdrawal = async () => {
        await userStore.withdrawal(userInfo.value);
        router.push('/')
    }

</script>

<template>
        <!--
            https://vuetifyjs.com/en/components/text-fields/#login-form
        -->
        <v-card
            class="myPage-form mx-auto pa-8"
            elevation="8"
            rounded="lg"
        >

            <div class="text-subtitle-1 text-medium-emphasis">계정</div>

            <v-text-field
                density="compact"
                prepend-inner-icon="mdi-email-outline"
                variant="outlined"
                readonly
                v-model="userInfo.id"
            ></v-text-field>


            <div class="text-subtitle-1 text-medium-emphasis">이름</div>

            <v-text-field
                density="compact"
                prepend-inner-icon="mdi-account-tag"
                variant="outlined"
                readonly
                v-model="userInfo.name"
            ></v-text-field>


            <div class="text-subtitle-1 text-medium-emphasis d-flex align-center justify-space-between">
                현재 비밀번호
            </div>

            <v-text-field
                :append-inner-icon="currentPasswordVisible ? 'mdi-eye-off' : 'mdi-eye'"
                :type="currentPasswordVisible ? 'text' : 'password'"
                density="compact"
                placeholder="현재 비밀번호"
                prepend-inner-icon="mdi-lock-outline"
                variant="outlined"
                @click:append-inner="currentPasswordVisible = !currentPasswordVisible"
                v-model="userInfo.currentPassword"
            ></v-text-field>

            <div class="text-subtitle-1 text-medium-emphasis d-flex align-center justify-space-between">
                새로운 비밀번호
            </div>

            <v-text-field
                :append-inner-icon="newPasswordVisible ? 'mdi-eye-off' : 'mdi-eye'"
                :type="newPasswordVisible ? 'text' : 'password'"
                density="compact"
                placeholder="새로운 비밀번호"
                prepend-inner-icon="mdi-lock-outline"
                variant="outlined"
                @click:append-inner="newPasswordVisible = !newPasswordVisible"
                v-model="userInfo.newPassword"
            ></v-text-field>

            <v-btn
                block
                class="mb-8"
                color="blue"
                size="large"
                variant="tonal"
                @click="modify"
            >
                수정하기
            </v-btn>

            <v-btn
                block
                class="mb-8"
                color="red"
                size="large"
                variant="tonal"
                @click="$router.push({name: 'main'})"
            >
                취소
            </v-btn>

            <v-btn
                block
                class="mb-8"
                color="red-darken-4"
                size="large"
                @click="withdrawal"
            >
                회원 탈퇴
            </v-btn>
        </v-card>
</template>

<style scoped>
.myPage-form {
    max-width: 30rem;
    max-height: 40rem;
}
</style>