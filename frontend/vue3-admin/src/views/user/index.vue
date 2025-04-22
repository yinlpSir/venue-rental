<script setup>
    import {getAllUser,banned,unbanned} from "@/api/api.user"
    import { ElMessage } from "element-plus";
    import {onMounted, reactive} from "vue"

    const users = reactive({
        total:0,
        query:{
            pageSize:6,
            currentPage:1
        },
        data:[]
    })

    const handleBanned = async(userId)=>{
        await banned(userId).then(res=>{
            if(res){
                ElMessage({
                    message:'已拉黑',
                    type:"success"
                })
            }else{
                ElMessage({
                    message:'无法拉黑管理员',
                    type:"warning"
                })
            }
        })
        await handleUsersData()
    }

    const handleUnbanned = async(userId)=>{
        await unbanned(userId).then(res=>{
            if(res){
                ElMessage({
                    message:'已取消拉黑',
                    type:"success"
                })
            }
        })
        await handleUsersData()
    }

    const handleUsersData = ()=>{
        getAllUser().then(res=>{
            users.data = res
        })
    }

    onMounted(()=>{
        handleUsersData()
    })

</script>

<template>
    <div class="users-container">
        <el-table :data="users.data" style="width: 100%;border-radius: 10px; color: black;background-color: var(--el-bg-color-page);">
            <el-table-column prop="username" label="用户名" width="180" />
            <el-table-column prop="email" label="邮箱"  />
            <el-table-column prop="role" label="身份"  >
                <template #default="scope">
                    <el-tag
                        :type="scope.row.role == 'null' ? 'warning' : 'success'"
                        disable-transitions
                    >{{ scope.row.role == 'null'? '无权限':scope.row.role }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="role" label="状态" >
                <template #default="scope">
                    <el-tag
                        :type="scope.row.role == 'null' ? 'warning' : 'success'"
                        disable-transitions
                    >{{ scope.row.role == 'null'? '不可用':'可用' }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="registerDate" label="注册时间" />
            <el-table-column label="操作">
                <template #default="scope">
                    <el-button size="small" type="danger" v-if="scope.row.role !== 'null'" @click="handleBanned(scope.row.id)">拉黑</el-button>
                    <el-button size="small" type="primary" v-else @click="handleUnbanned(scope.row.id)">取消拉黑</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<style scoped>
.users-container {
    border: 1px solid #dde2eb;
    border-radius: 10px;
    box-shadow: 0px 0px 12px rgba(0, 0, 0, .12);
}
</style>