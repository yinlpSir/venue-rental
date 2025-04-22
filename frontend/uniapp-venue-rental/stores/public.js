import {defineStore} from 'pinia'

import {ref} from 'vue'

/**
 * 公共数据
 */
export const usePublicStore = defineStore('public',()=>{
	
	// 头像
	const avatarSrc = ref('https://img1.baidu.com/it/u=743583283,2537785447&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=800')
	
	const setAvatarSrc = (value)=>{
		avatarSrc.value = value
	}
	
	const email = ref('')
	
	const setEmail = (value)=>{
		email.value = value
	}
	
	const canRentalDates = ref([])
	
	const setCanRentalDates = (value)=>{
		canRentalDates.value = value
	}
	
	return {
		avatarSrc,
		setAvatarSrc,
		email,
		setEmail,
		canRentalDates,
		setCanRentalDates
	}
})