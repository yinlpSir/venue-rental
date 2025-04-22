<script setup>
    import {getUser} from "@/api/api.user"
    import {authStoe} from "@/stores/index"
    import { onMounted, reactive, ref } from "vue";

    const as = authStoe()

    const userInfo = reactive({})

    onMounted(()=>{
        getUser(as.token).then(res=>{
            Object.assign(userInfo, res)
        })
    })
</script>

<template>
    <div class="home">
        <div class="item">
            <span class="item-title">用户名</span>
            <span style="color: green;">{{ userInfo.username }}</span>
        </div>
        <div class="item">
            <span class="item-title">邮箱</span>
            <span>{{ userInfo.email }}</span>
        </div>
        <div class="item">
            <span class="item-title">权限</span>
            <span>{{ userInfo.role }}</span>
        </div>
        <div class="item">
            <span class="item-title">注册时间</span>
            <span>{{ userInfo.registerDate }}</span>
        </div>
    </div>
</template>

<style scoped>
    .home{
        border: 1px solid #dde2eb;
        border-radius: 10px;
        box-shadow: 0px 0px 12px rgba(0, 0, 0, .12);
    }
    .home .item{
        margin: 8px 20px;
        padding: 6px 6px;
    }
    .home .item .item-title{
        display: inline-block;
        width: 100px;
        margin-right: 50px;
        border-right: 1px solid #000;
        font-weight: bold;
        font-size: 18px;
    }
</style>