'use strict';

import ajax from './ajax.js';

export const delImg = async (id) => {
    return await ajax({
        method:'POST',
        url:`/image/del/${id}`
    })
}