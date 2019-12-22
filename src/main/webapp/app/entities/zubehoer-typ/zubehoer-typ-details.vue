<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="zubehoerTyp">
                <h2 class="jh-entity-heading"><span v-text="$t('gdb3App.zubehoerTyp.detail.title')">ZubehoerTyp</span> {{zubehoerTyp.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span v-text="$t('gdb3App.zubehoerTyp.bezeichnung')">Bezeichnung</span>
                    </dt>
                    <dd>
                        <span>{{zubehoerTyp.bezeichnung}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.zubehoerTyp.gueltigBis')">Gueltig Bis</span>
                    </dt>
                    <dd>
                        <span>{{zubehoerTyp.gueltigBis}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.zubehoerTyp.technologie')">Technologie</span>
                    </dt>
                    <dd>
                        <span v-text="$t('gdb3App.Technologie.' + zubehoerTyp.technologie)">{{zubehoerTyp.technologie}}</span>
                    </dd>
                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
                </button>
                <router-link v-if="zubehoerTyp.id" :to="{name: 'ZubehoerTypEdit', params: {zubehoerTypId: zubehoerTyp.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./zubehoer-typ-details.component.ts">
</script>
