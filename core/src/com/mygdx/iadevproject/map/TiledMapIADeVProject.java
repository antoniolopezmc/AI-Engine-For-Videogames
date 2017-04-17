package com.mygdx.iadevproject.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Clase creada para implementar el método 'renderObject' y poder renderizar
 * los objetos del mapa, ya que por defecto, este método está vacío
 */
public class TiledMapIADeVProject extends OrthogonalTiledMapRenderer {

	public TiledMapIADeVProject(TiledMap map) {
		super(map);
	}

	@Override
    public void render() {
        beginRender();
        for (MapLayer layer : map.getLayers()) {
            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {
                    renderTileLayer((TiledMapTileLayer)layer);
                } else {
                    for (MapObject object : layer.getObjects()) {
                        renderObject(object);
                    }
                }
            }
        }
        endRender();
    }
	
	@Override
	public void renderObject(MapObject object) {
		if(object instanceof TextureMapObject) {
            TextureMapObject textureObj = (TextureMapObject) object;
            batch.draw(textureObj.getTextureRegion(), textureObj.getX(), textureObj.getY());
        }
	}
}
