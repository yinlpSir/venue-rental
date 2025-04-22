<script setup>
import { onMounted, reactive, ref,toRaw } from 'vue'
import { Delete, Download, Plus, ZoomIn } from '@element-plus/icons-vue'
import { authStoe } from '@/stores'
import { ElMessage } from 'element-plus'
import {delImg} from '@/api/api.img'
import {createField} from '@/api/api.field'
import Map from "@/views/map/index.vue"

const auth = authStoe()

// js region代码块
// #region 上传图片相关api

const uploadRef = ref()

const imgUploadUrl = ref('http://anitsunat.natapp1.cc/image/upload')

// 设置上传请求头
const headers = {
    Authorization:'Bearer '+ auth.token
}

// 对话框显示
const dialogVisible = ref(false)

const dialogImageUrl = ref('')

const disabled = ref(false)

/**
 * 上传成功后的执行函数
 * 
 * response     图片添加成功后返回的id
 */
const uploadSuccess = (response,uploadFiles)=>{
    uploadFiles.raw.uploadId = response
    fieldInfo.imageId.push(response)
}

const uploadError = (response,uploadFiles)=>{
    ElMessage.error(JSON.parse(response.message).message)
}

// 手动移除文件
const handleRemove = (file,uploadRef) => {
    delImg(file.raw.uploadId).then(res=>{
        uploadRef.handleRemove(file) // 删除上传图片组件里的文件
        fieldInfo.imageId.splice(fieldInfo.imageId.indexOf(file.raw.uploadId),1)
    }).catch(err=>console.log('移除文件失败'))
}

// 将图片显示到dialog对话框里
const handlePictureCardPreview = (file) => {
    dialogImageUrl.value = file.url
    dialogVisible.value = true
}

// 下载文件
const handleDownload = (file) => {}

// #endregion

const formRef = ref(null)

const fieldInfo = reactive({
    name: "",
    description: "",
    address: "",
    imageId: [],
    status: true,
    price: 0,
    position:{
        lng:0.0,
        lat:0.0,
    }
})

const rules = reactive({
    name: [
        { required: true, message: '请选择场地名字', trigger: 'blur' },
    ],
    description: [
        { required: true, message: '请输入场地描述', trigger: 'blur' },
    ],
    address: [
        { required: true, message: '请为场地设置个地址', trigger: 'blur' },
    ],
    images: [
        { 
            required: true, 
            validator:(rule,value,callback)=>{
                if(fieldInfo.imageId.length == 0) {
                    callback(new Error("请上传场地图片"))
                }else callback()
            },
            trigger: 'blur' 
        },
    ],
    price:[
        {
            required: true, 
            validator:(rule,value,callback)=>{
                if(value < 0) callback(new Error("请输入价格"))
                else callback()
            },
            trigger: 'blur' 
        },
    ]
})

const onSubmit = (formRef) => {
    if (!formRef) return
    formRef.validate((valid) => {
        if (valid) {
            console.log(fieldInfo.address)
            createField(toRaw(fieldInfo)).then(res=>{
                ElMessage.success('创建成功!')
                fieldInfo.imageId = [] // 先清空这里，不然执行reset()方法时又把图片删了
                reset(formRef)
            }).catch(err=>console.log('场地创建失败'))
        }
    })
}

const reset = (formRef) => {
    if (!formRef) return
    formRef.resetFields()
    if(fieldInfo.imageId.length > 0) {
        // 删除数据库照片
        fieldInfo.imageId.forEach(id => delImg(id))
    }
    // 清空图片上传组件里的 图片文件
    uploadRef.value.clearFiles()
    fieldInfo.imageId = []
}

// #region 地图相关

onMounted(()=>{
    const BMapGL = window.BMapGL
    var map =new BMapGL.Map("map")
    var point = new BMapGL.Point(112.9217642764148,28.144761157329636) // 经度 | 纬度 
    map.centerAndZoom(point,15)
    map.enableScrollWheelZoom(true)

    // 创建定位控件
    var locationControl = new BMapGL.LocationControl({
        // 控件的停靠位置（可选，默认左上角）
        anchor: BMAP_ANCHOR_TOP_RIGHT,
        // 控件基于停靠位置的偏移量（可选）
        offset: new BMapGL.Size(20, 20)
    });
    // 将定位控件添加到地图上
    map.addControl(locationControl);

    // 添加缩放控件
    var zoomControl = new BMapGL.ZoomControl();  
    map.addControl(zoomControl); // 将缩放控件添加到地图上

    // 创建城市选择控件
    var cityControl = new BMapGL.CityListControl({
        // 控件的停靠位置（可选，默认左上角）
        anchor: BMAP_ANCHOR_TOP_LEFT,
        // 控件基于停靠位置的偏移量（可选）
        offset: new BMapGL.Size(10, 5)
    });
    // 将控件添加到地图上
    map.addControl(cityControl);

      // 创建地理编码实例      
    var myGeo = new BMapGL.Geocoder(); 

    map.addEventListener('click', function (e) {
        fieldInfo.position.lng = e.latlng.lng
        fieldInfo.position.lat = e.latlng.lat
        map.clearOverlays()
        // 创建点标记
        var marker = new BMapGL.Marker(new BMapGL.Point(e.latlng.lng, e.latlng.lat));
        map.addOverlay(marker)
        
        myGeo.getLocation(e.latlng, function(rs){      
            if (rs){      
                // var addComp = rs.addressComponents;
                // fieldInfo.address = addComp.province+addComp.city+addComp.district+addComp.street+addComp.streetNumber
                fieldInfo.address = rs.address
            } 
        })

        var opts = {
            position: e.latlng, // 指定文本标注所在的地理位置
            offset: new BMapGL.Size(20, -30) // 设置文本偏移量
        };
        // 创建文本标注对象
        var label = new BMapGL.Label(`${e.latlng.lng},${e.latlng.lat}`, opts);
        label.setStyle({
            color: 'blue',
            borderRadius: '5px',
            borderColor: '#ccc',
            padding: '10px',
            fontSize: '16px',
            height: '20px',
            lineHeight: '20px',
            fontFamily: '微软雅黑'
        });
        map.addOverlay(label);
    });

})

// #endregion
</script>

<template>
    <div class="create-field-container">
        <el-form ref="formRef" class="form" :model="fieldInfo" :rules="rules" label-width="120px">
            <el-form-item label="场地名称" prop="name">
                <el-input v-model="fieldInfo.name" placeholder="请输入场地名称~" />
            </el-form-item>
            <el-form-item label="场地描述" prop="description">
                <el-input v-model="fieldInfo.description" :autosize="{minRows:3}" type="textarea" placeholder="请描述一下您的场地~" />
            </el-form-item>
            <el-form-item label="场地地址" prop="address">
                <div class="Map" id="map" style="width: 700px; height: 400px;"></div>
                <!-- <input id="map-search" v-model="fieldInfo.address" style="width: 600px;" placeholder="请输入场地地址~" /> -->
            </el-form-item>
            <el-form-item label="场地图片" prop="images">
                <el-upload 
                    ref="uploadRef" 
                    :action="imgUploadUrl" 
                    list-type="picture-card" 
                    :auto-upload="true" 
                    :headers="headers"
                    :on-success="uploadSuccess"
                    :on-error="uploadError"
                >
                    <el-icon>
                        <Plus />
                    </el-icon>

                    <template #file="{ file }">
                        <div>
                            <img class="el-upload-list__item-thumbnail" :src="file.url" alt="" />
                            <span class="el-upload-list__item-actions">
                                <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
                                    <el-icon><zoom-in /></el-icon>
                                </span>
                                <span v-if="!disabled" class="el-upload-list__item-delete"
                                    @click="handleDownload(file)">
                                    <el-icon>
                                        <Download />
                                    </el-icon>
                                </span>
                                <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleRemove(file,uploadRef)">
                                    <el-icon>
                                        <Delete />
                                    </el-icon>
                                </span>
                            </span>
                        </div>
                    </template>
                </el-upload>

                <el-dialog width="70%" v-model="dialogVisible">
                    <img width="100%" w-full :src="dialogImageUrl" alt="预览图片" />
                </el-dialog>
            </el-form-item>
            <el-form-item label="价格(单位:天)" prop="price">
                <el-input-number v-model="fieldInfo.price" :min="0" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit(formRef)">创建</el-button>
                <el-button @click="reset(formRef)">重置</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<style scoped>
.create-field-container {
    border: 1px solid #dde2eb;
    border-radius: 10px;
    box-shadow: 0px 0px 12px rgba(0, 0, 0, .12);
}

.create-field-container .form {
    width: 80%;
    margin: 30px auto;
}

</style>