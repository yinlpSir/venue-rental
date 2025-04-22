'use strict';

import ajax from './ajax.js';

export const login = async(data) => {
    return await ajax({
        method:'POST',
        url:'/login',
        data,
    })
}

export const getUser = async () => {
    return await ajax({
        method:'GET',
        url:'/self',
    })
}

export const getUserById = async (id) => {
    return await ajax({
        method:'GET',
        url:`/user/${id}`,
    })
}

export const getAllUser = async () => {
    return await ajax({
        method:'GET',
        url:`/getall`,
    })
}

/**
 * 拉黑用户
 * @param {*} userId 
 * @returns 
 */
export const banned = async (userId) => {
    return await ajax({
        method:'GET',
        url:`/ban/${userId}`,
    })
}

export const unbanned = async (userId) => {
    return await ajax({
        method:'GET',
        url:`/unban/${userId}`,
    })
}

export const teacherAuthentication = (id,isAuthenticated) => {
    return ajax({
        method:'PUT',
        url:`/user/teacher/authenticate/${id}/${isAuthenticated}`,
    })
}

export const getStudent = (data) => {
    return ajax({
        method:'GET',
        url:'/user/student/all',
        params:data,
    })
}