import {createStore} from 'vuex'

const store = createStore({
    state: {
        isLoading: false,
        openVcIssuedModal: false,
        openModal: { // alert 모달창
            isOpen: false,
            title: '',
            content: '',
            onConfirm: null,
        },
    },
    mutations: {
        setLoading(state, value) {
            state.isLoading = value;
        },
        setOpenModal(state, payload) {
            state.openModal = {
                ...state.openModal,
                isOpen: payload.isOpen,
                title: payload.title,
                content: payload.content,
                onConfirm: payload.onConfirm || null,
                onCancel: payload.onCancel || null // '취소' 콜백 추가
            };
        },
        closeModal(state) {
            state.openModal = {
                ...state.openModal,
                isOpen: false,
                onConfirm: null,
                onCancel: null
            };
        }
    },
})
export default store;
