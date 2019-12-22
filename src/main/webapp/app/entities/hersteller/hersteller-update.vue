<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="gdb3App.hersteller.home.createOrEditLabel" v-text="$t('gdb3App.hersteller.home.createOrEditLabel')">Create or edit a Hersteller</h2>
                <div>
                    <div class="form-group" v-if="hersteller.id">
                        <label for="id" v-text="$t('global.field.id')">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="hersteller.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.hersteller.bezeichnung')" for="hersteller-bezeichnung">Bezeichnung</label>
                        <input type="text" class="form-control" name="bezeichnung" id="hersteller-bezeichnung"
                            :class="{'valid': !$v.hersteller.bezeichnung.$invalid, 'invalid': $v.hersteller.bezeichnung.$invalid }" v-model="$v.hersteller.bezeichnung.$model"  required/>
                        <div v-if="$v.hersteller.bezeichnung.$anyDirty && $v.hersteller.bezeichnung.$invalid">
                            <small class="form-text text-danger" v-if="!$v.hersteller.bezeichnung.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.hersteller.bezeichnung.minLength" v-bind:value="$t('entity.validation.minlength')">
                                This field is required to be at least 1 characters.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.hersteller.bezeichnung.maxLength" v-bind:value="$t('entity.validation.maxlength')">
                                This field cannot be longer than 200 characters.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.hersteller.gueltigBis')" for="hersteller-gueltigBis">Gueltig Bis</label>
                        <div class="input-group">
                            <input id="hersteller-gueltigBis" type="date" class="form-control" name="gueltigBis"  :class="{'valid': !$v.hersteller.gueltigBis.$invalid, 'invalid': $v.hersteller.gueltigBis.$invalid }"
                            v-model="$v.hersteller.gueltigBis.$model"  />
                        </div>
                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.hersteller.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./hersteller-update.component.ts">
</script>
