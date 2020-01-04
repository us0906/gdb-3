<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="gdb3App.betriebsstaette.home.createOrEditLabel" v-text="$t('gdb3App.betriebsstaette.home.createOrEditLabel')">Create or edit a Betriebsstaette</h2>
                <div>
                    <div class="form-group" v-if="betriebsstaette.id">
                        <label for="id" v-text="$t('global.field.id')">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="betriebsstaette.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.betriebsstaette.bsnr')" for="betriebsstaette-bsnr">Bsnr</label>
                        <input type="text" class="form-control" name="bsnr" id="betriebsstaette-bsnr"
                            :class="{'valid': !$v.betriebsstaette.bsnr.$invalid, 'invalid': $v.betriebsstaette.bsnr.$invalid }" v-model="$v.betriebsstaette.bsnr.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.betriebsstaette.strasse')" for="betriebsstaette-strasse">Strasse</label>
                        <input type="text" class="form-control" name="strasse" id="betriebsstaette-strasse"
                            :class="{'valid': !$v.betriebsstaette.strasse.$invalid, 'invalid': $v.betriebsstaette.strasse.$invalid }" v-model="$v.betriebsstaette.strasse.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.betriebsstaette.hausnummer')" for="betriebsstaette-hausnummer">Hausnummer</label>
                        <input type="text" class="form-control" name="hausnummer" id="betriebsstaette-hausnummer"
                            :class="{'valid': !$v.betriebsstaette.hausnummer.$invalid, 'invalid': $v.betriebsstaette.hausnummer.$invalid }" v-model="$v.betriebsstaette.hausnummer.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.betriebsstaette.plz')" for="betriebsstaette-plz">Plz</label>
                        <input type="text" class="form-control" name="plz" id="betriebsstaette-plz"
                            :class="{'valid': !$v.betriebsstaette.plz.$invalid, 'invalid': $v.betriebsstaette.plz.$invalid }" v-model="$v.betriebsstaette.plz.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.betriebsstaette.ort')" for="betriebsstaette-ort">Ort</label>
                        <input type="text" class="form-control" name="ort" id="betriebsstaette-ort"
                            :class="{'valid': !$v.betriebsstaette.ort.$invalid, 'invalid': $v.betriebsstaette.ort.$invalid }" v-model="$v.betriebsstaette.ort.$model" />
                    </div>

                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.betriebsstaette.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./betriebsstaette-update.component.ts">
</script>
