<script setup>
import { AsideBar, NavBar } from './components'
import { layoutStoe } from '@/stores'
// 加载网络请求时要打开loading dialog 防止多操作
import { viewLoading } from '@/stores/reactive'

// 通用组件布局
const layout = layoutStoe()

const handleCollapse = () => {
    layout.collapse = !layout.collapse
}

</script>

<template>
    <el-container class="ea-layout">
        <!-- 菜单栏遮罩层 -->
        <div v-if="!layout.collapse" class="ea-aside-mask" @click="handleCollapse" />
        <!-- 菜单栏展示 -->
        <el-aside class="ea-aside" :class="{ 'ea-aside-collapse': layout.collapse }">
            <div class="logo">
                <h2 v-if="!layout.collapse" class="logotitle">
                    场地租赁系统
                </h2>
            </div>
            <AsideBar />
        </el-aside>
        <!-- 右侧内容层 -->
        <el-container class="ea-select">
            <!-- 顶部工具层 -->
            <el-header>
                <NavBar />
            </el-header>
            <!-- 实际路由展示层 -->
            <router-view v-slot="{ Component }">
                <div v-loading="viewLoading.status" class="ea-main" :element-loading-text="viewLoading.text">
                    <el-main>
                        <component :is="Component" />
                    </el-main>
                </div>
            </router-view>
        </el-container>
    </el-container>
</template>


