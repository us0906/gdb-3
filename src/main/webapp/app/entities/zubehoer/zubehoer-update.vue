<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="gdb3App.zubehoer.home.createOrEditLabel" v-text="$t('gdb3App.zubehoer.home.createOrEditLabel')">Create or edit a Zubehoer</h2>
                <div>
                    <div class="form-group" v-if="zubehoer.id">
                        <label for="id" v-text="$t('global.field.id')">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="zubehoer.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.zubehoer.bezeichnung')" for="zubehoer-bezeichnung">Bezeichnung</label>
                        <input type="text" class="form-control" name="bezeichnung" id="zubehoer-bezeichnung"
                            :class="{'valid': !$v.zubehoer.bezeichnung.$invalid, 'invalid': $v.zubehoer.bezeichnung.$invalid }" v-model="$v.zubehoer.bezeichnung.$model"  required/>
                        <div v-if="$v.zubehoer.bezeichnung.$anyDirty && $v.zubehoer.bezeichnung.$invalid">
                            <small class="form-text text-danger" v-if="!$v.zubehoer.bezeichnung.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.zubehoer.bezeichnung.minLength" v-bind:value="$t('entity.validation.minlength')">
                                This field is required to be at least 1 characters.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.zubehoer.bezeichnung.maxLength" v-bind:value="$t('entity.validation.maxlength')">
                                This field cannot be longer than 200 characters.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.zubehoer.gueltigBis')" for="zubehoer-gueltigBis">Gueltig Bis</label>
                        <div class="input-group">
                            <input id="zubehoer-gueltigBis" type="date" class="form-control" name="gueltigBis"  :class="{'valid': !$v.zubehoer.gueltigBis.$invalid, 'invalid': $v.zubehoer.gueltigBis.$invalid }"
                            v-model="$v.zubehoer.gueltigBis.$model"  />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.zubehoer.hersteller')" for="zubehoer-hersteller">Hersteller</label>
                        <select class="form-control" id="zubehoer-hersteller" name="hersteller" v-model="$v.zubehoer.herstellerId.$model" required>
                            <option v-if="!zubehoer.herstellerId" v-bind:value="null" selected></option>
                            <option v-bind:value="herstellerOption.id" v-for="herstellerOption in herstellers" :key="herstellerOption.id">{{herstellerOption.bezeichnung}}</option>
                        </select>
                    </div>
                    <div v-if="$v.zubehoer.herstellerId.$anyDirty && $v.zubehoer.herstellerId.$invalid">
                        <small class="form-text text-danger" v-if="!$v.zubehoer.herstellerId.required" v-text="$t('entity.validation.required')">
                            This field is required.
                        </small>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.zubehoer.zubehoerTyp')" for="zubehoer-zubehoerTyp">Zubehoer Typ</label>
                        <select class="form-control" id="zubehoer-zubehoerTyp" name="zubehoerTyp" v-model="$v.zubehoer.zubehoerTypId.$model" required>
                            <option v-if="!zubehoer.zubehoerTypId" v-bind:value="null" selected></option>
                            <option v-bind:value="zubehoerTypOption.id" v-for="zubehoerTypOption in zubehoerTyps" :key="zubehoerTypOption.id">{{zubehoerTypOption.bezeichnung}}</option>
                        </select>
                    </div>
                    <div v-if="$v.zubehoer.zubehoerTypId.$anyDirty && $v.zubehoer.zubehoerTypId.$invalid">
                        <small class="form-text text-danger" v-if="!$v.zubehoer.zubehoerTypId.required" v-text="$t('entity.validation.required')">
                            This field is required.
                        </small>
                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.zubehoer.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./zubehoer-update.component.ts">
</script>
