import store from '@/store/store';

export function openModal(title, content) {
    store.commit('setOpenModal', {
        isOpen: true,
        title,
        content,
    });
}

export function openModalCallback(title, content, callback) {
    store.commit('setOpenModal', {
        isOpen: true,
        title,
        content,
        onConfirm: callback,
    });
}

export function openModalCancelCallback(title, content, callback, cancelCallback) {
    store.commit('setOpenModal', {
        isOpen: true,
        title,
        content,
        onConfirm: callback,
        onCancel: cancelCallback,
    });
}
