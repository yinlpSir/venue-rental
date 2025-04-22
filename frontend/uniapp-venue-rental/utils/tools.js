
/**
 * Toast 显示方法
 */
export const showToast = (toastRef,params) => {
		toastRef.value.show({
			...params,
			complete() {
				// 如果params里有url则跳转到url
				params.url && uni.navigateTo({
					url: params.url  
				});
			}
		})
	}