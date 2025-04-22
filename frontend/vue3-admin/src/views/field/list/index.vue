<script setup>
    import {getFields,getFieldTotal,getImg,getCommentByFieldId,updateField,delField, checkField, getPosition} from "@/api/api.field"
    import {getUserById} from "@/api/api.user"
    import { onMounted, reactive, ref } from "vue";
    import { authStoe } from '@/stores'
    import { ElMessage } from "element-plus";

    const auth = authStoe()

    const fields = reactive({
        total:0, // 总共多少数据
        query:{
            pageSize:6, // 每页显示多少条数据
            currentPage:1 // 当前页
        },
        data:[]
    })


    // 场地图片 dialog 是否显示
    const showFieldImg = ref(false)

    const imgs = ref([])

    const handleShowFieldImg = (imgIds)=>{
        showFieldImg.value = true
        imgIds.forEach(e => {
            getImg(e).then(res=>{
                imgs.value.push(res)
            })
        });
    }

    const handleFieldImgClose = ()=>{
        imgs.value = []
        showFieldImg.value = false
    }


    // 场地评论 drawer 是否显示
    const showComment = ref(false)

    const showEmpty = ref(false)

    const comments = ref([])

    const handleShowComment = async(fieldId)=>{
        showComment.value = true
        await getCommentByFieldId(fieldId).then(res=>{
            comments.value = res
        })
        await comments.value.forEach(e=>{
            getUserById(e.userId).then(res=>{
                e.username = res.username
            })
        })
        if(comments.value.length == 0) showEmpty.value = true
    }

    const handleCommentClose = ()=>{
        comments.value = []
        showEmpty.value = false
        showComment.value = false
    }

    const confirmDelField = (fieldId)=>{
        delField(fieldId).then(res=>{
            if(res){
                ElMessage.success("删除成功!")
                handleFieldsData()
            }
        })
    }


    const handleChangePage = (value)=>{
        fields.query.currentPage = value
        handleFieldsData()
    }

    const handleFieldsData = async()=>{
        await getFields({
            page:fields.query.currentPage-1,
            size:fields.query.pageSize
        }).then(res=>{
            res.forEach(f => {
                checkField(f.id)
            })
        }).catch(err=>console.log('查询场地失败!'))
        await getFields({
            page:fields.query.currentPage-1,
            size:fields.query.pageSize
        }).then(res=>{
            fields.data = res
        }).catch(err=>console.log('查询场地失败!'))
        fields.data.forEach(f => {
            getPosition(f.id).then(res=>{
                f.position = res.position
            }).catch(err => console.log(err))
        })
        await getFieldTotal().then(res=>{
            fields.total = res
        })
    }

    const timeConvert = (datetimeArr)=>{
		const [year, month, day, hours, minutes, milliseconds] = datetimeArr; 
		return `${year}-${month}-${day} ${hours}:${minutes>=10?minutes:'0'+minutes}`
	}

    handleFieldsData()
</script>

<template>
    <div class="fields-container">
        <el-table :data="fields.data" style="width: 100%;border-radius: 10px; color: black;background-color: var(--el-bg-color-page);">
            <el-table-column prop="name" label="场地名称" width="180" />
            <el-table-column prop="description" label="场地描述"  />
            <el-table-column prop="price" label="场地价格" width="100">
                <template #default="scope">
                    ￥{{ scope.row.price }}/天
                </template>
            </el-table-column>
            <el-table-column prop="status" label="场地状态" width="80">
                <!--status 为true，表示可用状态-->
                <template #default="scope">
                    <el-tag
                        :type="scope.row.status == true ? 'success' : 'warning'"
                        disable-transitions
                    >{{ scope.row.status == true? '可租用':'不可租用' }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="address" label="场地位置" />
            <el-table-column prop="position" label="场地坐标" width="100">
                <!--status 为true，表示可用状态-->
                <template #default="scope">
                    <!-- ,{{ scope.row.position.lat }} -->
                    {{scope.row.position == null?'null':`${scope.row.position.lng},${scope.row.position.lat}`}}
                </template>
            </el-table-column>
            <el-table-column label="操作">
                <template #default="scope">
                    <el-button size="small" type="primary" @click="handleShowFieldImg(scope.row.imageId)">查看图片</el-button>
                    <el-button size="small" type="primary" @click="handleShowComment(scope.row.id)">查看评价</el-button>
                    <el-popconfirm @confirm="confirmDelField(scope.row.id)" title="确定要删除该场地吗？">
                        <template #reference>
                            <el-button size="small" type="danger" >删除</el-button>
                        </template>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>
        <div style="width: 100%;display: flex;justify-content: center;margin: 8px 0;">
            <el-pagination 
                layout="total, prev, pager, next" 
                background
                :page-size="fields.query.pageSize"
                :default-current-page="fields.query.currentPage"
                @current-change="handleChangePage"
                :total="fields.total" 
            />
        </div>
    </div>
    <!--场地图片 dialog-->
    <el-dialog v-model="showFieldImg" width="75%" title="场地图片" center @close="handleFieldImgClose">
        <el-image  v-for="(img,index) in imgs" :key="index" :src="img" fit="fill" style="display: block;width: 850px;height: 450px; margin: 2px auto;border: 2px solid #ccc;" />
    </el-dialog>

    <!--场地评论 抽屉-->
    <el-drawer v-model="showComment" title="评论" :with-header="false" @close="handleCommentClose">
        <div class="comments">
            <div class="comment" v-for="comment in comments" :key="comment.id">
                <el-avatar :size="50" src="https://img1.baidu.com/it/u=743583283,2537785447&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=800" />
                <div style="margin-left: 4px;">
					<p style="margin: 0;margin-top: 4px;margin-left: 2px;display: flex;align-items: flex-end;">
						<span style="font-size: 16px;">{{comment.username}}</span>
						<span style="margin-left: 4px;color:#a5a5a5;font-size: 12px;">
							发布于:{{timeConvert(comment.sendTime)}}
						</span>
					</p>
                    <el-rate v-model="comment.rate" disabled size="default"/>
					<p style="margin: 4px 0;overflow-wrap: anywhere;">{{comment.content
					}}</p>
				</div>
            </div>
        </div>
        <el-empty style="margin-top: 200px;" description="暂无评论" v-show="showEmpty" />
    </el-drawer>

</template>

<style scoped>
    .fields-container{
        border: 1px solid #dde2eb;
        border-radius: 10px;
        box-shadow: 0px 0px 12px rgba(0, 0, 0, .12);
    }
    .comments .comment{
        display: flex;
        margin: 26px 0;
    }
</style>