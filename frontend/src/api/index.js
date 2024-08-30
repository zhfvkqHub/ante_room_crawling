import axios from 'axios';
import {setInterceptors} from './common/interceptors';
import {openModal} from './common/modal';
import {handleError} from './common/responseHandlers';

function createInstance() {
    const instance = axios.create({
        baseURL: `${window.location.origin}/api`,
        headers: {
            'Content-Type': 'application/json',
        },
    });
    return setInterceptors(instance);
}

export const instance = createInstance()

export async function axiosGetNotice(page, size) {
    try {
        return instance.get(`/notice`, {
            params: {
                page: page - 1,
                size: size
            }
        });
    } catch (error) {
        const errorMessage = handleError(error);
        openModal("ERROR", errorMessage);
    }
}
