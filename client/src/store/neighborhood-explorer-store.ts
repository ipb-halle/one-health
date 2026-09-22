import { Instance, types } from 'mobx-state-tree';

export const NodeModel = types.model('NodeModel', {
    id: types.identifier,
    label: types.string,
    group: types.literal('nodes'),
    data: types.frozen<any>(),
    position: types.maybe(types.frozen<any>()),
    color: types.maybe(types.string),
});

export const EdgeModel = types.model('EdgeModel', {
    id: types.identifier,
    source: types.string,
    target: types.string,
    group: types.literal('edges'),
    data: types.frozen<any>(),
});

export const NeighborhoodExplorerStore = types
    .model('NeighborhoodExplorerStore', {
        ids: types.optional(types.array(types.string), []),
        nodes: types.optional(types.array(NodeModel), []),
        edges: types.optional(types.array(EdgeModel), []),
    })
    .views((self) => ({
        get elements() {
            return [...self.nodes, ...self.edges];
        },
    }))
    .actions((self) => ({
        setIds(value: string[]) {
            self.ids.replace(value);
        },

        addNodes(value: any[]) {
            const nextNodes = value.map((node: any) => {
                const data = node?.data ?? node ?? {};
                const id = String(data.id ?? node?.id ?? '');

                return NodeModel.create({
                    id,
                    label: String(data.label ?? node?.label ?? id),
                    group: 'nodes',
                    data: data ?? {},
                    position: node?.position ?? undefined,
                    color: data?.color ?? node?.color ?? undefined,
                });
            });

            self.nodes.replace([...self.nodes, ...nextNodes]);
            self.ids.replace(self.nodes.map((node) => node.id));
        },

        setGraphData(nodes: any[], edges: any[]) {
            const nextNodes = nodes.map((node: any) => {
                const data = node?.data ?? node ?? {};
                const id = String(data.id ?? node?.id ?? '');

                return NodeModel.create({
                    id,
                    label: String(data.label ?? node?.label ?? id),
                    group: 'nodes',
                    data: data ?? {},
                    position: node?.position ?? undefined,
                    color: data?.color ?? node?.color ?? undefined,
                });
            });

            const nextEdges = edges.map((edge: any) => {
                const data = edge?.data ?? edge ?? {};
                const source = String(data.source ?? edge?.source ?? '');
                const target = String(data.target ?? edge?.target ?? '');
                const label = String(
                    data.label ?? edge?.label ?? `${source}->${target}`,
                );
                const fallbackId = `${source}::${target}::${label}`;
                const id = String(data.id ?? edge?.id ?? fallbackId);

                return EdgeModel.create({
                    id,
                    source,
                    target,
                    group: 'edges',
                    data: data ?? {},
                });
            });

            self.nodes.replace(nextNodes);
            self.edges.replace(nextEdges);
            self.ids.replace(self.nodes.map((node) => node.id));
        },

        setFromStringifiedElements(value: string) {
            const parsed = JSON.parse(value);
            const elements = Array.isArray(parsed) ? parsed : [];
            const nodes = elements.filter((item: any) => item?.group === 'nodes');
            const edges = elements.filter((item: any) => item?.group === 'edges');

            const nextNodes = nodes.map((node: any) => {
                const data = node?.data ?? node ?? {};
                const id = String(data.id ?? node?.id ?? '');

                return NodeModel.create({
                    id,
                    label: String(data.label ?? node?.label ?? id),
                    group: 'nodes',
                    data: data ?? {},
                    position: node?.position ?? undefined,
                    color: data?.color ?? node?.color ?? undefined,
                });
            });

            const nextEdges = edges.map((edge: any) => {
                const data = edge?.data ?? edge ?? {};
                const source = String(data.source ?? edge?.source ?? '');
                const target = String(data.target ?? edge?.target ?? '');
                const label = String(
                    data.label ?? edge?.label ?? `${source}->${target}`,
                );
                const fallbackId = `${source}::${target}::${label}`;
                const id = String(data.id ?? edge?.id ?? fallbackId);

                return EdgeModel.create({
                    id,
                    source,
                    target,
                    group: 'edges',
                    data: data ?? {},
                });
            });

            self.nodes.replace(nextNodes);
            self.edges.replace(nextEdges);
            self.ids.replace(self.nodes.map((node) => node.id));
        },

        clear() {
            self.ids.replace([]);
            self.nodes.replace([]);
            self.edges.replace([]);
        },
    }));

export type INeighborhoodExplorerStore = Instance<typeof NeighborhoodExplorerStore>;
