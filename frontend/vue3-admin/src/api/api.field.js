'use strict';

import ajax from './ajax.js';

export const createField = async (data) => {
    return await ajax({
        method:'POST',
        url:`/field/create`,
        data
    })
}

/**
 * 分页获取场地
 * @param {*} data 
 * @returns 
 */
export const getFields = async (data) => {
    return await ajax({
        method:'POST',
        url:`/field/all`,
        data
    })
}

export const getFieldTotal = async () => {
    return await ajax({
        method:'GET',
        url:`/field/total`
    })
}

export const getImg = async (imageId) => {
    return await ajax({
        method:'GET',
        url:`/image/${imageId}`
    })
}

export const getCommentByFieldId = async (fieldId) => {
    return await ajax({
        method:'GET',
        url:`/comment/fieldId/${fieldId}`
    })
}

export const updateField = async (field) => {
    return await ajax({
        method:'POST',
        url:`/field/update`,
        data:field
    })
}

export const delField = async (fieldId) => {
    return await ajax({
        method:'POST',
        url:`/field/del/${fieldId}`
    })
}

export const checkField = async (fieldId) => {
    return await ajax({
        method:'POST',
        url:`/field/is/${fieldId}`
    })
}

export const getPosition = async (id) => {
    return await ajax({
        method:'POST',
        url:`/field/getPosition/${id}`
    })
}