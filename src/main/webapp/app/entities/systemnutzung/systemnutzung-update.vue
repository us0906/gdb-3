<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="gdb3App.systemnutzung.home.createOrEditLabel" v-text="$t('gdb3App.systemnutzung.home.createOrEditLabel')">Create or edit a Systemnutzung</h2>
                <div>
                    <div class="form-group" v-if="systemnutzung.id">
                        <label for="id" v-text="$t('global.field.id')">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="systemnutzung.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-bind:value="$t('gdb3App.systemnutzung.systeminstanz')" for="systemnutzung-systeminstanz">Systeminstanz</label>
                        <select class="form-control" id="systemnutzung-systeminstanz" name="systeminstanz" v-model="systemnutzung.systeminstanzId">
                            <option v-bind:value="null"></option>
                            <option v-bind:value="systeminstanzOption.id" v-for="systeminstanzOption in systeminstanzs" :key="systeminstanzOption.id">{{systeminstanzOption.id}}</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-bind:value="$t('gdb3App.systemnutzung.arzt')" for="systemnutzung-arzt">Arzt</label>
                        <select class="form-control" id="systemnutzung-arzt" name="arzt" v-model="systemnutzung.arztId">
                            <option v-bind:value="null"></option>
                            <option v-bind:value="arztOption.id" v-for="arztOption in arzts" :key="arztOption.id">{{arztOption.id}}</option>
                        </select>
                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.systemnutzung.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./systemnutzung-update.component.ts">
</script>
