<template>
  <Transition name="fade">
    <div v-if="isVisible" class="modal-overlay">
      <div class="modal-container modal-danger">
        <div class="modal-icon">
          <span class="icon-warning">⚠</span>
        </div>
        
        <h2 class="modal-title">{{ title || t('common.deleteConfirm') }}</h2>
        <p class="modal-message">{{ message || t('common.deleteConfirmMessage') }}</p>
        
        <div class="modal-actions">
          <button @click="cancel" class="btn-modal-secondary">
            {{ cancelLabel || t('common.cancel') }}
          </button>
          <button @click="confirm" class="btn-modal-danger">
            {{ confirmLabel || t('common.delete') }}
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
  title: String,
  message: String,
  cancelLabel: String,
  confirmLabel: String
})

const emit = defineEmits(['confirm', 'cancel'])

const isVisible = ref(props.isOpen)

watch(() => props.isOpen, (newVal) => {
  isVisible.value = newVal
})

const confirm = () => {
  isVisible.value = false
  emit('confirm')
}

const cancel = () => {
  isVisible.value = false
  emit('cancel')
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

.modal-container.modal-danger {
  border-left: 4px solid var(--danger-color);
}

.modal-icon {
  font-size: 3rem;
  line-height: 1;
  margin-bottom: 1rem;
}

.icon-warning {
  color: var(--danger-color);
  display: inline-block;
  width: 60px;
  height: 60px;
  line-height: 60px;
  border-radius: 50%;
  background: rgba(239, 68, 68, 0.1);
  font-size: 2rem;
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
  flex-wrap: wrap;
}

.btn-modal-secondary {
  background: transparent;
  color: var(--text-muted);
  border: 1px solid var(--text-muted);
  padding: 0.65rem 1.5rem;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.12s ease;
}

.btn-modal-secondary:hover {
  background: var(--background-color);
  color: var(--text-main);
  border-color: var(--text-main);
}

.btn-modal-danger {
  background: linear-gradient(135deg, #ef4444, #dc2626);
  color: white;
  border: none;
  padding: 0.65rem 1.5rem;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.12s ease;
}

.btn-modal-danger:hover {
  box-shadow: 0 8px 20px rgba(239, 68, 68, 0.3);
  transform: translateY(-2px);
}

.btn-modal-danger:active {
  transform: translateY(0);
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
