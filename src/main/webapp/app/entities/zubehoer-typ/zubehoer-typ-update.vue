<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="gdb3App.zubehoerTyp.home.createOrEditLabel" v-text="$t('gdb3App.zubehoerTyp.home.createOrEditLabel')">Create or edit a ZubehoerTyp</h2>
                <div>
                    <div class="form-group" v-if="zubehoerTyp.id">
                        <label for="id" v-text="$t('global.field.id')">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="zubehoerTyp.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.zubehoerTyp.bezeichnung')" for="zubehoer-typ-bezeichnung">Bezeichnung</label>
                        <input type="text" class="form-control" name="bezeichnung" id="zubehoer-typ-bezeichnung"
                            :class="{'valid': !$v.zubehoerTyp.bezeichnung.$invalid, 'invalid': $v.zubehoerTyp.bezeichnung.$invalid }" v-model="$v.zubehoerTyp.bezeichnung.$model"  required/>
                        <div v-if="$v.zubehoerTyp.bezeichnung.$anyDirty && $v.zubehoerTyp.bezeichnung.$invalid">
                            <small class="form-text text-danger" v-if="!$v.zubehoerTyp.bezeichnung.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.zubehoerTyp.bezeichnung.minLength" v-bind:value="$t('entity.validation.minlength')">
                                This field is required to be at least 1 characters.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.zubehoerTyp.bezeichnung.maxLength" v-bind:value="$t('entity.validation.maxlength')">
                                This field cannot be longer than 200 characters.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.zubehoerTyp.gueltigBis')" for="zubehoer-typ-gueltigBis">Gueltig Bis</label>
                        <div class="input-group">
                            <input id="zubehoer-typ-gueltigBis" type="date" class="form-control" name="gueltigBis"  :class="{'valid': !$v.zubehoerTyp.gueltigBis.$invalid, 'invalid': $v.zubehoerTyp.gueltigBis.$invalid }"
                            v-model="$v.zubehoerTyp.gueltigBis.$model"  />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.zubehoerTyp.technologie')" for="zubehoer-typ-technologie">Technologie</label>
                        <select class="form-control" name="technologie" :class="{'valid': !$v.zubehoerTyp.technologie.$invalid, 'invalid': $v.zubehoerTyp.technologie.$invalid }" v-model="$v.zubehoerTyp.technologie.$model" id="zubehoer-typ-technologie"  required>
                            <option value="SONO" v-bind:label="$t('gdb3App.Technologie.SONO')">SONO</option>
                            <option value="BILD" v-bind:label="$t('gdb3App.Technologie.BILD')">BILD</option>
                        </select>
                        <div v-if="$v.zubehoerTyp.technologie.$anyDirty && $v.zubehoerTyp.technologie.$invalid">
                            <small class="form-text text-danger" v-if="!$v.zubehoerTyp.technologie.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                        </div>
                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.zubehoerTyp.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./zubehoer-typ-update.component.ts">
</script>
