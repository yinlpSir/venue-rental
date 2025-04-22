export default [
    {
        path: '',
        name: 'home',
        component: () => import('@/views/home/index.vue'),
        meta: {
            title: '主页',
            icon: 'User'
        }
    },
    {
        path: 'user',
        name: 'user',
        component: () => import('@/views/user/index.vue'),
        meta: {
            title: '用户管理',
            icon: 'User'
        }
    },
    {
        path: 'field',
        redirect:'field/list', // 方面 NavBar 的搜索菜单那
        meta: {
            title: '场地管理',
            icon: 'Operation'
        },
        children:[
            {
                path: 'list',
                name: 'fieldList',
                component: () => import('@/views/field/list/index.vue'),
                meta: {
                    title: '场地列表',
                    icon: 'List'
                }
            },
            {
                path: 'create',
                name: 'fieldCreate',
                component: () => import('@/views/field/create/index.vue'),
                meta: {
                    title: '场地创建',
                    icon: 'BrushFilled'
                }
            },
        ]
    },
]