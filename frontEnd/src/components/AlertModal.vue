<template>
  <Transition name="fade">
    <div v-if="isVisible" class="modal-overlay">
      <div class="modal-container" :class="`modal-${type}`">
        <div class="modal-icon">
          <span v-if="type === 'success'" class="icon-success">✓</span>
          <span v-else-if="type === 'error'" class="icon-error">✕</span>
          <span v-else class="icon-info">ℹ</span>
        </div>
        
        <h2 class="modal-title">{{ title }}</h2>
        <p class="modal-message">{{ message }}</p>
        
        <div class="modal-actions">
          <button @click="close" :class="['btn-modal-primary', `btn-${type}`]">
            {{ actionLabel || t('common.ok') }}
          </button>
        </div>
      </div>
    </div>
  </Transition>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useI18n } from '../composables/useI18n'

const { t } = useI18n()

const props = defineProps({
  isOpen: Boolean,
  type: {
    type: String,
    default: 'info' // 'success', 'error', 'info'
  },
  title: String,
  message: String,
  actionLabel: String,
  autoClose: {
    type: Number,
    default: 0 // milliseconds, 0 = manual
  }
})

const emit = defineEmits(['close'])

const isVisible = ref(props.isOpen)

watch(() => props.isOpen, (newVal) => {
  isVisible.value = newVal
  if (newVal && props.autoClose > 0) {
    setTimeout(() => {
      close()
    }, props.autoClose)
  }
})

const close = () => {
  isVisible.value = false
  emit('close')
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(11, 18, 32, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  backdrop-filter: blur(2px);
}

.modal-container {
  background: var(--surface-color);
  border-radius: var(--radius);
  padding: 2rem;
  box-shadow: 0 20px 60px rgba(11, 18, 32, 0.15);
  max-width: 420px;
  width: 90%;
  text-align: center;
  position: relative;
  border: 1px solid rgba(11, 18, 32, 0.04);
}

.modal-container.modal-success {
  border-left: 4px solid var(--success-color);
}

.modal-container.modal-error {
  border-left: 4px solid var(--danger-color);
}

.modal-container.modal-info {
  border-left: 4px solid var(--accent-color);
}

.modal-icon {
  font-size: 3rem;
  line-height: 1;
  margin-bottom: 1rem;
}

.icon-success {
  color: var(--success-color);
  display: inline-block;
  width: 60px;
  height: 60px;
  line-height: 60px;
  border-radius: 50%;
  background: rgba(22, 163, 74, 0.1);
}

.icon-error {
  color: var(--danger-color);
  display: inline-block;
  width: 60px;
  height: 60px;
  line-height: 60px;
  border-radius: 50%;
  background: rgba(239, 68, 68, 0.15);
  font-weight: bold;
}

.icon-info {
  color: var(--accent-color);
  display: inline-block;
  width: 60px;
  height: 60px;
  line-height: 58px;
  border-radius: 50%;
  background: rgba(37, 99, 235, 0.1);
  font-weight: bold;
}

.modal-title {
  margin: 0 0 0.5rem;
  font-size: 1.125rem;
  font-weight: 700;
  color: var(--text-main);
}

.modal-message {
  margin: 0 0 1.5rem;
  color: var(--text-muted);
  line-height: 1.5;
  font-size: 0.95rem;
}

.modal-actions {
  display: flex;
  gap: 0.75rem;
  justify-content: center;
}

.btn-modal-primary {
  background: var(--accent-color);
  color: white;
  border: none;
  padding: 0.65rem 1.5rem;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.12s ease, box-shadow 0.12s ease;
}

.btn-modal-primary:hover {
  box-shadow: 0 8px 20px rgba(37, 99, 235, 0.25);
  transform: translateY(-2px);
}

.btn-modal-primary:active {
  transform: translateY(0);
}

/* Button type variants */
.btn-success {
  background: var(--success-color);
}

.btn-success:hover {
  box-shadow: 0 8px 20px rgba(22, 163, 74, 0.3);
}

.btn-error {
  background: linear-gradient(135deg, #ef4444, #dc2626);
}

.btn-error:hover {
  box-shadow: 0 8px 20px rgba(239, 68, 68, 0.3);
}

/* Animations */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
