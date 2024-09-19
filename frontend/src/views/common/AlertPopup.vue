<template>
  <div class="alertArea">
    <div class="alert" tabindex="0" ref="messageAlert">
      <div class="alertIconWrap">
      </div>
      <div class="textArea">
        <p class="tit" v-text="title"></p>
        <p v-html="content"></p>
      </div>
      <div class="btnArea">
        <button @click="confirmAction" type="button" class="basic orange full">
          확인
        </button>

        <button @click="cancelAction" class="basic grey" v-if="onCancel">
          취소
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import {mapMutations, mapState} from 'vuex';

export default {
  computed: {
    ...mapState({
      isOpen: state => state.openModal.isOpen,
      title: state => state.openModal.title,
      content: state => state.openModal.content,
      onConfirm: state => state.openModal.onConfirm,
      onCancel: state => state.openModal.onCancel
    })
  },
  methods: {
    ...mapMutations(['setOpenModal']),
    close() {
      this.setOpenModal({
        isOpen: false,
      });
    },
    confirmAction() {
      if (this.onConfirm) {
        this.onConfirm();
      }
      this.setOpenModal({ isOpen: false });
    },
    cancelAction() {
      if (this.onCancel) {
        this.onCancel();
      }
      this.setOpenModal({ isOpen: false });
    }
  }
}
</script>

<style>
.alertArea {
  position: fixed;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  top: 0;
  left: 0;
  z-index: 9999999;
}

.alert {
  position: relative;
  width: 350px;
  padding: 22px;
  background: #fff;
  border-radius: 15px;
  text-align: center;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
}

.alert.other {
  width: 450px;
}

.alertIconWrap {
  width: 50px;
  height: 50px;
  text-align: left;
}

.alertIconWrap img {
  max-width: 100%;
  height: auto;
}

.textArea {
  padding: 20 20px 30px;
}

.alert p {
  font-size: 1.2rem;
  margin-bottom: 5px;
  line-height: 1.5;
  word-break: break-word;
}

.alert p.tit {
  font-size: 1.4rem;
  font-weight: bold;
  margin-bottom: 10px;
}

.alert p.errercode {
  color: #686B72;
}

.alert p span {
  font-weight: bold;
}

.alert a {
  color: var(--main-color);
  text-decoration: underline;
  font-weight: bold;
}

.btnArea {
  display: flex;
  justify-content: center;
  padding: 16px 0;
}

.btnArea button {
  padding: 12px 24px;
  font-size: 1.6rem;
  border-radius: 8px;
  background-color: orange;
  color: #fff;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btnArea button:hover {
  background-color: darkorange;
}

.btnArea button {
  margin-right: 10px;
}

.grey {
  background-color : gray;
}
</style>
