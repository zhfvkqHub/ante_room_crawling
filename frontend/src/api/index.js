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

export async function axiosGetNotice(params) {
    try {
        return instance.get(`/notice`, {
            params
        });
    } catch (error) {
        const errorMessage = handleError(error);
        openModal("ERROR", errorMessage);
    }
}

export const axiosGetLastCrawlingTime = () => {
    return instance.get('/notice/last-crawled-time');
};

export const axiosGetSites = () => {
    return instance.get('/notice/sites');
}

export const axiosGetConstituencies = () => {
    return instance.get('/notice/constituencies');
}


// push

export async function sendTokenToServer(token) {
    try {
        return instance.post('/push/token', {
            token
        });
    } catch (error) {
        const errorMessage = handleError(error);
        openModal("ERROR", errorMessage);
    }
}
