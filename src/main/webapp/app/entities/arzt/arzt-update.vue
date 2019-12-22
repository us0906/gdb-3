<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="gdb3App.arzt.home.createOrEditLabel" v-text="$t('gdb3App.arzt.home.createOrEditLabel')">Create or edit a Arzt</h2>
                <div>
                    <div class="form-group" v-if="arzt.id">
                        <label for="id" v-text="$t('global.field.id')">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="arzt.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.arzt.lanr')" for="arzt-lanr">Lanr</label>
                        <input type="text" class="form-control" name="lanr" id="arzt-lanr"
                            :class="{'valid': !$v.arzt.lanr.$invalid, 'invalid': $v.arzt.lanr.$invalid }" v-model="$v.arzt.lanr.$model"  required/>
                        <div v-if="$v.arzt.lanr.$anyDirty && $v.arzt.lanr.$invalid">
                            <small class="form-text text-danger" v-if="!$v.arzt.lanr.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.arzt.lanr.minLength" v-bind:value="$t('entity.validation.minlength')">
                                This field is required to be at least 7 characters.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.arzt.lanr.maxLength" v-bind:value="$t('entity.validation.maxlength')">
                                This field cannot be longer than 7 characters.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.arzt.titel')" for="arzt-titel">Titel</label>
                        <input type="text" class="form-control" name="titel" id="arzt-titel"
                            :class="{'valid': !$v.arzt.titel.$invalid, 'invalid': $v.arzt.titel.$invalid }" v-model="$v.arzt.titel.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.arzt.vorname')" for="arzt-vorname">Vorname</label>
                        <input type="text" class="form-control" name="vorname" id="arzt-vorname"
                            :class="{'valid': !$v.arzt.vorname.$invalid, 'invalid': $v.arzt.vorname.$invalid }" v-model="$v.arzt.vorname.$model"  required/>
                        <div v-if="$v.arzt.vorname.$anyDirty && $v.arzt.vorname.$invalid">
                            <small class="form-text text-danger" v-if="!$v.arzt.vorname.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.arzt.nachname')" for="arzt-nachname">Nachname</label>
                        <input type="text" class="form-control" name="nachname" id="arzt-nachname"
                            :class="{'valid': !$v.arzt.nachname.$invalid, 'invalid': $v.arzt.nachname.$invalid }" v-model="$v.arzt.nachname.$model"  required/>
                        <div v-if="$v.arzt.nachname.$anyDirty && $v.arzt.nachname.$invalid">
                            <small class="form-text text-danger" v-if="!$v.arzt.nachname.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                        </div>
                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.arzt.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./arzt-update.component.ts">
</script>
